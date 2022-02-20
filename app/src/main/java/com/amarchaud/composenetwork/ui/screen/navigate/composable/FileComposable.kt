package com.amarchaud.composenetwork.ui.screen.navigate.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.composenetwork.ui.screen.navigate.model.FileUiModel
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme


@Composable
fun FileComposable(
    item: FileUiModel,
    onFileLongPressed: (FileUiModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onFileLongPressed(item)
                    }
                )
            }
            .padding(start = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = item.name)
            Text(text = item.modificationDate)
            item.contentType?.let { Text(text = it) }
        }

    }
}

@Preview
@Composable
private fun PreviewFile() {
    ComposeNetworkTheme {
        FileComposable(item = FileUiModel(
            id = "",
            parentId = "",
            name = "File",
            modificationDate = "10 janvier 2020",
            contentType = "jpg"
        ), onFileLongPressed = {})
    }
}
