package com.amarchaud.composenetwork.ui.screen.navigate.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.amarchaud.composenetwork.R
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme

@Composable
fun AlertDialogNewFolder(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onNewFolder: (String) -> Unit,
) {
    val (text, onValueChange) = remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { onNewFolder(text) }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
                Button(onClick = onCancel) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        },
        text = {
            TextField(
                value = text,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
        },
        title = {
            Text(text = stringResource(id = R.string.dialog_create_folder_message))
        },
    )
}

@Preview
@Composable
private fun PreviewAlertDialogDelete() {
    ComposeNetworkTheme {
        AlertDialogNewFolder(
            onDismissRequest = { },
            onCancel = { },
            onNewFolder = {}
        )
    }
}