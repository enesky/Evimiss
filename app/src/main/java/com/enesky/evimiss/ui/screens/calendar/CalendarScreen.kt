package com.enesky.evimiss.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enesky.evimiss.main.MainScaffold
import com.enesky.evimiss.ui.custom.calendar.MyCalendar
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.white

@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //var name by remember { mutableStateOf("") }
        val name: String by calendarViewModel.name.observeAsState(initial = "")

        MyCalendar()
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScaffold(
        content = { CalendarScreen(calendarViewModel = CalendarViewModel()) }
    )
}