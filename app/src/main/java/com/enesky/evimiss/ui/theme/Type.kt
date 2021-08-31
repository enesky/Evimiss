package com.enesky.evimiss.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.enesky.evimiss.R

val Nunito = FontFamily(
        Font(R.font.nunito_regular),
        Font(R.font.nunito_light, weight = FontWeight.Light),
        Font(R.font.nunito_semibold, weight = FontWeight.SemiBold),
        Font(R.font.nunito_bold, weight = FontWeight.Bold)
)

val Typography = Typography(
       defaultFontFamily = Nunito
)