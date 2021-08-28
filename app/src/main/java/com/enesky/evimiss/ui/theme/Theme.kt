package com.enesky.evimiss.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val colorPalette = darkColors(
    primary = vesuvius,
    primaryVariant = vesuvius,
    secondary = capeCod
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