package com.amarchaud.composenetwork.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = amComposeNetworkBlue,
    primaryVariant = amComposeNetworkBlueVariant,
    secondary = amComposeNetworkPurple,
    secondaryVariant = amComposeNetworkPurpleVariant,
    background = amComposeNetworkBlueVariant,
)

private val LightColorPalette = lightColors(
    primary = amComposeNetworkBlue,
    primaryVariant = amComposeNetworkBlueVariant,
    secondary = amComposeNetworkPurple,
    secondaryVariant = amComposeNetworkPurpleVariant,
    background = amComposeNetworkBlueVariant
)

@Composable
fun ComposeNetworkTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}