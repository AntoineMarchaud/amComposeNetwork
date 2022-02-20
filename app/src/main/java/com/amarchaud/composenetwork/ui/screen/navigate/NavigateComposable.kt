package com.amarchaud.composenetwork.ui.screen.navigate

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amarchaud.composenetwork.R
import com.amarchaud.composenetwork.ui.screen.navigate.composable.*
import com.amarchaud.composenetwork.ui.screen.navigate.model.FolderContentUiModel
import com.amarchaud.composenetwork.ui.screen.navigate.model.FolderUiModel
import com.amarchaud.composenetwork.ui.utils.BoxWithBackground
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun NavigateComposable(
    onConnectionClicked: () -> Unit,
    viewModel: NavigateViewModel = viewModel()
) {
    val context = LocalContext.current
    val isConnected by viewModel.isConnected.collectAsState()
    val folderContent by viewModel.folderContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val imageToDisplay by viewModel.imageToDisplay.collectAsState()
    val lastError by viewModel.lastError.collectAsState()

    NavigateScreen(
        modifier = Modifier.fillMaxSize(),
        isConnected = isConnected,
        imageToDisplay = imageToDisplay,
        isLoading = isLoading,
        folderContent = folderContent,
        onBackPressed = {
            viewModel.onBack()
        },
        onConnectionClicked = onConnectionClicked,
        onDisconnectClick = {
            viewModel.disconnect()
        },
        onFolderClick = { folder ->
            viewModel.refreshFolder(folder.name, folder.id, folder.parentId)
        },
        onDownload = {
            viewModel.downloadById(it)
        },
        onDeleteElement = {
            viewModel.deleteById(it)
        },
        onSwipeRefresh = {
            viewModel.refreshFolder(it.title, it.currentId, it.parentId)
        },
        onImageClose = {
            viewModel.closeImage()
        },
        onNewFolder = {
            viewModel.createNewFolder(it)
        },
        onImageSelected = { imageName, data ->
            viewModel.createNewFile(imageName, data)
        }
    )

    // basic error management
    lastError?.let { error ->
        Toast.makeText(context, stringResource(id = error.message), Toast.LENGTH_LONG).show()
    }
}

/**
 * Use to save a Color object for Composable creation
 */
private val ColorSaver = run {
    val alphaKey = "alpha"
    val rKey = "R"
    val gKey = "G"
    val bKey = "B"
    mapSaver(
        save = { mapOf(alphaKey to it.alpha, rKey to it.red, gKey to it.red, bKey to it.blue) },
        restore = {
            Color(
                alpha = it[alphaKey] as Float,
                red = it[rKey] as Float,
                green = it[gKey] as Float,
                blue = it[bKey] as Float
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NavigateScreen(
    modifier: Modifier,
    onBackPressed: () -> Unit,
    isLoading: Boolean,
    imageToDisplay: ByteArray?,
    isConnected: Boolean?,
    folderContent: FolderContentUiModel?,
    onSwipeRefresh: (FolderContentUiModel) -> Unit,
    onDisconnectClick: () -> Unit,
    onConnectionClicked: () -> Unit,
    onFolderClick: (FolderUiModel) -> Unit,
    onDownload: (String) -> Unit,
    onDeleteElement: (String) -> Unit,
    onImageClose: () -> Unit,
    onNewFolder: (String) -> Unit,
    onImageSelected: (String, ByteArray) -> Unit
) {

    var statusBarColor by rememberSaveable(stateSaver = ColorSaver) { mutableStateOf(Color.Transparent) }
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    // Update status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
    }

    ProvideWindowInsets {

        BoxWithBackground(modifier = modifier) {

            AnimatedVisibility(
                visible = isConnected != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                when (isConnected) {
                    true -> {

                        statusBarColor = when (folderContent?.title) {
                            null -> Color.Transparent
                            else -> Color.Red
                        }

                        NavigateConnected(
                            modifier = Modifier.fillMaxSize(),
                            folderContent = folderContent,
                            isLoading = isLoading,
                            onBackPressed = onBackPressed,
                            onDeleteElement = onDeleteElement,
                            onDisconnectClick = onDisconnectClick,
                            onDownload = onDownload,
                            onFolderClick = onFolderClick,
                            onImageSelected = onImageSelected,
                            onNewFolder = onNewFolder,
                            onSwipeRefresh = onSwipeRefresh
                        )
                    }
                    false -> {

                        statusBarColor = Color.Transparent

                        NavigateNotConnected(
                            modifier = Modifier.fillMaxSize(),
                            onConnectionClicked = onConnectionClicked
                        )
                    }
                    else -> {
                        statusBarColor = Color.Transparent
                    }
                }
            }

            // display full screen image
            imageToDisplay?.let {
                statusBarColor = Color.Transparent
                BoxWithImage(
                    modifier = Modifier.fillMaxSize(),
                    onImageClose = onImageClose,
                    imageRaw = it
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NavigateConnected(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    isLoading: Boolean,
    folderContent: FolderContentUiModel?,
    onSwipeRefresh: (FolderContentUiModel) -> Unit,
    onDisconnectClick: () -> Unit,
    onFolderClick: (FolderUiModel) -> Unit,
    onDownload: (String) -> Unit,
    onDeleteElement: (String) -> Unit,
    onNewFolder: (String) -> Unit,
    onImageSelected: (String, ByteArray) -> Unit
) {
    val swipeState = rememberSwipeRefreshState(false)
    swipeState.isRefreshing = isLoading

    val (displayNewFolder, onDisplayNewFolder) = remember { mutableStateOf(false) }
    val (displayDeleteAlertDialog, onDisplayDeleteAlertDialog) = remember {
        mutableStateOf(
            false
        )
    }
    val (displayAlertDialogFile, onDisplayAlertDialogFile) = remember { mutableStateOf(false) }
    var itemIdAction by remember { mutableStateOf("") }


    SwipeRefresh(
        modifier = modifier,
        state = swipeState,
        onRefresh = {
            folderContent?.let {
                onSwipeRefresh(it)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            LazyColumn {

                folderContent?.title?.let {

                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.Red)
                        ) {

                            folderContent.parentId?.let {

                                Button(
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterStart)
                                        .padding(start = 16.dp),
                                    onClick = onBackPressed,
                                ) {
                                    Text(text = stringResource(id = R.string.navigate_screen_back))
                                }
                            }


                            Text(
                                text = it,
                                modifier = Modifier.align(alignment = Alignment.Center)
                            )
                        }
                    }
                }

                folderContent?.folders?.let {
                    itemsIndexed(
                        items = it,
                        key = { _, item -> item.hashCode() }) { _, oneItem ->
                        FolderComposable(
                            item = oneItem,
                            onFolderClick = onFolderClick,
                            onFolderLongPressed = { folder ->
                                itemIdAction = folder.id
                                onDisplayDeleteAlertDialog(true)
                            }
                        )

                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                }

                folderContent?.files?.let {

                    items(
                        items = it
                    ) { oneItem ->

                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                if (dismissValue == DismissValue.DismissedToStart) {
                                    // call api when swipe left
                                    onDeleteElement(oneItem.id)
                                }
                                true
                            })

                        // Display a SwipeToDismiss for each file
                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { _ -> FractionalThreshold(0.5f) },
                            background = {

                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.LightGray
                                        DismissValue.DismissedToStart -> Color.Red
                                        else -> Color.Red
                                    }
                                )

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },
                            dismissContent = {
                                FileComposable(
                                    item = oneItem,
                                    onFileLongPressed = { oneItem ->
                                        itemIdAction = oneItem.id
                                        onDisplayAlertDialogFile(true)
                                    }
                                )
                            }
                        )

                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            BottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter),
                onDisconnectClick = onDisconnectClick,
                onImageSelected = onImageSelected,
                onDisplayNewFolder = onDisplayNewFolder
            )
        }

        if (displayDeleteAlertDialog) {
            AlertDialogStandard(
                title = stringResource(id = R.string.dialog_delete_folder_title),
                text = stringResource(id = R.string.dialog_delete_folder_message),
                onDismissRequest = {
                    onDisplayDeleteAlertDialog(false)
                },
                onLeftButtonClicked = {
                    onDisplayDeleteAlertDialog(false)
                    onDeleteElement(itemIdAction)
                },
                onRightButtonClicked = {
                    onDisplayDeleteAlertDialog(false)
                }
            )
        }

        if (displayAlertDialogFile) {

            AlertDialogStandard(
                title = stringResource(id = R.string.dialog_file_choose_title),
                onDismissRequest = {
                    onDisplayAlertDialogFile(false)
                },
                leftButtonText = stringResource(id = R.string.dialog_file_choose_button_download),
                rightButtonText = stringResource(id = R.string.dialog_file_choose_button_delete),
                onLeftButtonClicked = {
                    onDisplayAlertDialogFile(false)
                    onDownload(itemIdAction)
                },
                onRightButtonClicked = {
                    onDisplayAlertDialogFile(false)
                    onDeleteElement(itemIdAction)
                }
            )
        }

        if (displayNewFolder) {
            AlertDialogNewFolder(
                modifier = Modifier.width(200.dp),
                onCancel = {
                    onDisplayNewFolder(false)
                },
                onDismissRequest = {
                    onDisplayNewFolder(false)
                },
                onNewFolder = {
                    onNewFolder(it)
                    onDisplayNewFolder(false)
                }
            )
        }
    }

}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    onDisconnectClick: () -> Unit,
    onDisplayNewFolder: (Boolean) -> Unit,
    onImageSelected: (String, ByteArray) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {

            Button(onClick = { onDisconnectClick() }) {
                Text(text = stringResource(id = R.string.navigate_screen_disconnect))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onDisplayNewFolder(true) }) {
                Text(text = stringResource(id = R.string.navigate_screen_folder_creation))
            }
        }

        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri -> imageUri = uri }

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick image")
        }

        imageUri?.let { uri ->
            imageUri = null
            val iStream = context.contentResolver.openInputStream(uri)
            iStream?.readBytes()?.let { byteArray ->
                getFileName(context, uri)?.let { filename ->
                    onImageSelected(filename, byteArray)
                }
            }
        }
    }

}

@Composable
private fun NavigateNotConnected(
    modifier: Modifier = Modifier,
    onConnectionClicked: () -> Unit,
) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier.align(alignment = Alignment.Center),
            onClick = { onConnectionClicked() }) {
            Text(text = stringResource(id = R.string.navigate_screen_connection))
        }
    }
}

/**
 * Return complete filename + extension of the file contained in the URI
 */
fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )
        cursor.use {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    result = cursor.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result!!.substring(cut + 1)
        }
    }
    return result
}


