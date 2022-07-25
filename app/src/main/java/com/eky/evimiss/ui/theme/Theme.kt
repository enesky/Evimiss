package com.eky.evimiss.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colorPalette = darkColors(
    primary = primary,
    primaryVariant = primaryDark,
    secondary =  secondary,
    secondaryVariant = secondaryDark,
    background =  primary,
    surface =  primary,
    error =  onError,
    onPrimary =  Color.White,
    onSecondary =  Color.White,
    onBackground =  Color.White,
    onSurface =  Color.White,
    onError =  Color.White
)

@Composable
fun EvimissTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = colorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}