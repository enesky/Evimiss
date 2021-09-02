package com.enesky.evimiss.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CalendarScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var name by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .paddingFromBaseline(top = 50.dp)
        ) {
            Text(
                text = "Hello",
                modifier = Modifier.padding(bottom = 8.dp),
                style = typography.h5
            )
            if (name.isNotEmpty())
                Text(
                    text = " $name !",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = typography.h5
                )
        }
        Text(
            text = "Welcome to the Evimiss",
            modifier = Modifier.padding(bottom = 8.dp),
            style = typography.body1
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("") },
            placeholder = { Text("Enter your name") },
            textStyle = typography.body1
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendarScreen()
}