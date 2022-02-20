package com.amarchaud.composenetwork.ui.screen.login

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.amarchaud.composenetwork.R
import com.amarchaud.composenetwork.domain.usecase.ConnectUseCase
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ConnectedError(@StringRes val message: Int) {
    object ApiNotAuthorized : ConnectedError(message = R.string.error_login)
    object GenericError : ConnectedError(message = R.string.error_generic_error)
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val connectUseCase: ConnectUseCase
) : AndroidViewModel(application) {

    /**
     * StateFlow
     */
    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val _lastError = MutableStateFlow<ConnectedError?>(null)
    val lastError = _lastError.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * Jobs
     */
    private var jobConnect: Job? = null

    fun connect(login: String, password: String) {

        jobConnect?.cancel()
        jobConnect = viewModelScope.launch {

            _lastError.value = null
            _isLoading.value = true

            connectUseCase.run(login, password).fold(
                { _isConnected.value = true },
                {
                    if (it is UseCaseError.ApiNotAuthorize) {
                        _lastError.value = ConnectedError.ApiNotAuthorized
                    } else {
                        _lastError.value = ConnectedError.GenericError
                    }
                }
            )

            _isLoading.value = false
        }
    }
}