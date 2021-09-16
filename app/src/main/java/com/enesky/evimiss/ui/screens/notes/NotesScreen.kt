package com.enesky.evimiss.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enesky.evimiss.R
import com.enesky.evimiss.ui.screens.main.MainScaffold
import com.enesky.evimiss.ui.theme.white
import com.enesky.evimiss.ui.theme.primary

@Composable
fun NotesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(R.string.title_notes),
            fontWeight = FontWeight.Bold,
            color = white,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    MainScaffold(
        content = { NotesScreen() }
    )
}