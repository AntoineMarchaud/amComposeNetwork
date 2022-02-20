package com.amarchaud.composenetwork.ui.screen.navigate.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme


@Composable
fun AlertDialogStandard(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    title: String? = null,
    text: String? = null,
    rightButtonText: String = stringResource(id = android.R.string.cancel),
    leftButtonText: String = stringResource(id = android.R.string.ok),
    onRightButtonClicked: () -> Unit,
    onLeftButtonClicked: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { onLeftButtonClicked() }) {
                    Text(text = leftButtonText)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onRightButtonClicked() }) {
                    Text(text = rightButtonText)
                }
            }
        },
        text = {
            text?.let {
                Text(text = it)
            }
        },
        title = {
            title?.let {
                Text(text = it)
            }
        },
    )
}

@Preview
@Composable
private fun PreviewAlertDialogDelete() {
    ComposeNetworkTheme {
        AlertDialogStandard(
            title = "title",
            text = "text",
            onDismissRequest = { },
            onLeftButtonClicked = {},
            onRightButtonClicked = {}
        )
    }
}