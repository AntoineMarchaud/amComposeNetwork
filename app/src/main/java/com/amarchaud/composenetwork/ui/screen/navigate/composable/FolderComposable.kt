package com.amarchaud.composenetwork.ui.screen.navigate.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.composenetwork.ui.screen.navigate.model.FolderUiModel
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderComposable(
    item: FolderUiModel,
    onFolderClick: (FolderUiModel) -> Unit,
    onFolderLongPressed: (FolderUiModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color.Yellow)
            .combinedClickable(
                onClick = { onFolderClick(item) },
                onLongClick = { onFolderLongPressed(item) },
            )
            .padding(start = 16.dp)
    ) {
        Text(text = item.name, modifier = Modifier.align(Alignment.CenterStart))
    }
}

@Preview
@Composable
private fun PreviewFolder() {
    ComposeNetworkTheme {
        FolderComposable(item = FolderUiModel(
            id = "",
            parentId = "",
            name = "Folder"
        ), onFolderClick = {}, onFolderLongPressed = {})
    }
}