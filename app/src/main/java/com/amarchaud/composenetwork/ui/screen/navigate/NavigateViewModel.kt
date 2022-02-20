package com.amarchaud.composenetwork.ui.screen.navigate

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.amarchaud.composenetwork.R
import com.amarchaud.composenetwork.domain.usecase.*
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError
import com.amarchaud.composenetwork.ui.screen.navigate.model.FolderContentUiModel
import com.amarchaud.composenetwork.ui.screen.navigate.model.toFileUiModel
import com.amarchaud.composenetwork.ui.screen.navigate.model.toFolderUiModel
import com.amarchaud.composenetwork.ui.screen.navigate.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

// basic error management
sealed class ErrorUi(@StringRes var message: Int) {
    object ErrorGeneric : ErrorUi(message = R.string.error_generic_error)
    object ErrorNoFolder : ErrorUi(message = R.string.error_no_folder)
    object ErrorItemDoesNotExist : ErrorUi(message = R.string.error_item_does_not_exist)
    object ErrorItemNotAFile : ErrorUi(message = R.string.error_item_not_a_file)
    object ErrorParentDoesNotExist : ErrorUi(message = R.string.error_parent_item_does_not_exist)
    object ErrorItemAlreadyExist : ErrorUi(message = R.string.error_item_already_exist)
}

@HiltViewModel
class NavigateViewModel @Inject constructor(
    application: Application,
    private val getMasterFolderUseCase: GetMasterFolderUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    private val getFolderContentUseCase: GetFolderContentUseCase,
    private val downloadByIdUseCase: DownloadByIdUseCase,
    private val deleteByIdUseCase: DeleteByIdUseCase,
    private val postFolderUseCase: PostFolderUseCase,
    private val postFileUseCase: PostFileUseCase
) : AndroidViewModel(application) {

    // save for cache
    private var _allContent = listOf<FolderContentUiModel>()

    /**
     * StateFlow
     */
    private val _currentFolderContent = MutableStateFlow<FolderContentUiModel?>(null)
    val folderContent = _currentFolderContent.asStateFlow()

    private val _isConnected = MutableStateFlow<Boolean?>(null)
    val isConnected = _isConnected.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _imageToDisplay = MutableStateFlow<ByteArray?>(null)
    var imageToDisplay = _imageToDisplay.asStateFlow()

    private var _lastError = MutableStateFlow<ErrorUi?>(null)
    var lastError = _lastError.asStateFlow()

    /**
     * Jobs
     */
    private var currentJob: Job? = null

    /**
     * Refresh the content of a folder
     * @param title the name of the current folder
     * @param currentId the id of the current folder
     * @param parentId the id of the parent folder. if null, it is the master folder
     */
    fun refreshFolder(title: String, currentId: String, parentId: String?) {
        resetLastError()

        currentJob?.cancel()
        currentJob = viewModelScope.launch {

            _isLoading.value = true

            getFolderContentUseCase.run(currentId).fold(
                {
                    _currentFolderContent.value = it.toUiModel(
                        title = title,
                        currentId = currentId,
                        parentId = parentId
                    ).also { uiModel ->
                        // refresh cache
                        if (_allContent.isNotEmpty()) {
                            val currentLast = _allContent.last()
                            if (currentLast.currentId == currentId) {
                                _allContent = _allContent.dropLast(1)
                            }
                        }
                        _allContent = _allContent.plus(uiModel)
                    }
                },
                {
                    _lastError.value = ErrorUi.ErrorGeneric
                }
            )

            _isLoading.value = false
        }
    }

    /**
     * Get the previous folder
     */
    fun onBack() {
        resetLastError()

        viewModelScope.launch {

            // remove current content
            _allContent = _allContent.dropLast(1)

            // display new current
            _allContent.last().let {

                // display cache immediately
                _currentFolderContent.value = it

                // then refresh view
                refreshFolder(it.title, it.currentId, it.parentId)
            }
        }
    }

    /**
     * Disconnect
     */
    fun disconnect() {
        resetLastError()

        viewModelScope.launch {
            disconnectUseCase.run()
        }
    }

    /**
     * Download a file by id
     * @param id the id of the file/folder
     */
    fun downloadById(id: String) {
        resetLastError()

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true

            downloadByIdUseCase.run(id).fold(
                {
                    _imageToDisplay.value = it
                },
                {
                    _lastError.value = when (it) {
                        is UseCaseError.BadRequest -> ErrorUi.ErrorItemNotAFile
                        is UseCaseError.ItemNotFound -> ErrorUi.ErrorItemDoesNotExist
                        else -> ErrorUi.ErrorGeneric
                    }
                }
            )

            _isLoading.value = false
        }
    }

    /**
     * Delete a folder/file by id
     * @param id the id of the file/folder
     */
    fun deleteById(id: String) {
        resetLastError()

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true

            deleteByIdUseCase.run(id).fold(
                {
                    // refresh current view
                    _currentFolderContent.value = _currentFolderContent.value?.apply {
                        this.folders.removeAll { it.id == id }
                        this.files.removeAll { it.id == id }
                    }?.also {
                        // refresh cache
                        _allContent = _allContent.dropLast(1).plus(it)
                    }
                },
                {
                    _lastError.value = when (it) {
                        is UseCaseError.ItemNotFound -> ErrorUi.ErrorItemDoesNotExist
                        else -> ErrorUi.ErrorGeneric
                    }
                }
            )

            _isLoading.value = false
        }
    }

    /**
     * Callback when a image is closed
     */
    fun closeImage() {
        _imageToDisplay.value = null
    }

    /**
     * Create a new folder
     * @param folderName the name of the new folder
     */
    fun createNewFolder(folderName: String) {
        resetLastError()

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true

            _currentFolderContent.value?.run {

                postFolderUseCase.run(
                    folderId = this.currentId,
                    folderName = folderName
                ).fold(
                    {
                        // refresh
                        _currentFolderContent.value = _currentFolderContent.value?.apply {
                            this.folders.add(it.toFolderUiModel())
                        }?.also {
                            // refresh cache
                            _allContent = _allContent.dropLast(1).plus(it)
                        }
                    },
                    {
                        _lastError.value = when (it) {
                            is UseCaseError.BadRequest -> ErrorUi.ErrorItemAlreadyExist
                            is UseCaseError.ItemNotFound -> ErrorUi.ErrorParentDoesNotExist
                            else -> ErrorUi.ErrorGeneric
                        }
                    }
                )
            } ?: run { _lastError.value = ErrorUi.ErrorNoFolder }

            _isLoading.value = false
        }
    }

    /**
     * Add a new file (i.e an image here)
     * @param imageName the name of the new folder
     * @param data the content of the file
     */
    fun createNewFile(imageName: String, data: ByteArray) {
        resetLastError()

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true

            _currentFolderContent.value?.let {

                postFileUseCase.run(
                    folderId = it.currentId,
                    fileName = imageName,
                    data = data
                ).fold(
                    {
                        // refresh
                        _currentFolderContent.value = _currentFolderContent.value?.apply {
                            this.files.add(it.toFileUiModel())
                        }?.also {
                            // refresh cache
                            _allContent = _allContent.dropLast(1).plus(it)
                        }
                    },
                    {
                        _lastError.value = when (it) {
                            is UseCaseError.BadRequest -> ErrorUi.ErrorItemAlreadyExist
                            is UseCaseError.ItemNotFound -> ErrorUi.ErrorParentDoesNotExist
                            else -> ErrorUi.ErrorGeneric
                        }
                    }
                )
            }

            _isLoading.value = false
        }
    }

    init {
        viewModelScope.launch {
            getMasterFolderUseCase.run().distinctUntilChanged().collect { me ->
                _isConnected.value = (me != null)
                me?.let {
                    refreshFolder(
                        title = it.rootItem.name, // master name
                        currentId = it.rootItem.id, // master id
                        parentId = null // no parent id
                    )
                }
            }
        }
    }

    /**
     * Only reset the error stateFlow. Must be called before each new call
     */
    private fun resetLastError() {
        _lastError.value = null
    }
}
