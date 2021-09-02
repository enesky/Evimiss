package com.enesky.evimiss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enesky.evimiss.R
import com.enesky.evimiss.ui.theme.EvimissTheme

@Composable
fun NotesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primaryColor))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Notes",
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.secondaryDarkColor),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    EvimissTheme {
        NotesScreen()
    }
}