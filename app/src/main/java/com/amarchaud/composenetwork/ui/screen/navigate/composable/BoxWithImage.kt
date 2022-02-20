package com.amarchaud.composenetwork.ui.screen.navigate.composable

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun BoxWithImage(
    modifier: Modifier = Modifier,
    imageRaw: ByteArray,
    onImageClose: () -> Unit
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = BitmapFactory.decodeByteArray(imageRaw, 0, imageRaw.size)
            ), contentDescription = "ImageDownloaded"
        )

        Button(
            onClick = onImageClose,
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 16.dp)
        ) {
            Text(text = "Close")
        }
    }
}

@Preview
@Composable
private fun PreviewAlertDialogDelete() {
    ComposeNetworkTheme {
        BoxWithImage(imageRaw = ByteArray(0)) {

        }
    }
}
