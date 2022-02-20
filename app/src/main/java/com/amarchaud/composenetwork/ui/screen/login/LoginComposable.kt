package com.amarchaud.composenetwork.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.composenetwork.R
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme
import com.amarchaud.composenetwork.ui.utils.BoxWithBackground
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginComposable(
    viewModel: LoginViewModel,
    isConnectedOk: () -> Unit,
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val error by viewModel.lastError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    if (isConnected) {
        isConnectedOk()
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    LoginScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = isLoading,
        onButtonConnect = { login, password ->
            viewModel.connect(login = login, password = password)
        })

    // basic error management
    error?.let {
        Toast.makeText(context, stringResource(id = it.message), Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onButtonConnect: (String, String) -> Unit,
) {
    val (login, onLoginChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPassWordChanged) = rememberSaveable { mutableStateOf("") }

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    ProvideWindowInsets {
        BoxWithBackground(modifier = modifier) {

            val keyboardController = LocalSoftwareKeyboardController.current

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextField(
                        value = login,
                        onValueChange = onLoginChange,
                        label = { Text(stringResource(id = R.string.login_screen_login)) },
                        placeholder = { Text(stringResource(id = R.string.login_screen_login)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = onPassWordChanged,
                        label = { Text(stringResource(id = R.string.login_screen_password)) },
                        placeholder = { Text(stringResource(id = R.string.login_screen_password)) },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        trailingIcon = {
                            val image =
                                if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                Icon(imageVector = image, "Icon visibility")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(
                        onClick = {
                            onButtonConnect(login, password)
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.login_screen_connect))
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                }
            }

        }
    }
}

@Preview
@Composable
private fun PreviewHeader() {
    ComposeNetworkTheme {
        LoginScreen(
            modifier = Modifier.fillMaxWidth(),
            onButtonConnect = { _, _ -> },
            isLoading = true
        )
    }
}
