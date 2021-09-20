package com.enesky.evimiss.ui.screens.calendar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enesky.evimiss.ui.custom.calendar.MyCalendar
import com.enesky.evimiss.ui.screens.main.MainScaffold
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalAnimationApi
@Composable
fun CalendarScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyCalendar()
    }
}


@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScaffold(
        content = { CalendarScreen() }
    )
}