package com.enesky.evimiss.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enesky.evimiss.main.MainScaffold
import com.enesky.evimiss.ui.theme.primary

@Composable
fun ExpenseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(5F),
            text = "Harcamalar",
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenPreview() {
    MainScaffold(
        content = { ExpenseScreen() }
    )
}