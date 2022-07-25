package com.eky.evimiss.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eky.evimiss.ui.custom.calendar.MyCalendar
import com.eky.evimiss.ui.screens.main.MainScaffold

@Composable
fun CalendarScreen() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyCalendar()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScaffold(
        content = { CalendarScreen() }
    )
}