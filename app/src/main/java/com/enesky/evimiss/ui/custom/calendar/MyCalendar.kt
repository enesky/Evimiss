package com.enesky.evimiss.ui.custom.calendar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enesky.evimiss.R
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.secondaryLight
import com.enesky.evimiss.utils.clickableWithoutRipple
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@Composable
fun MyCalendar() {
    calMonth()
}

@Composable
fun calHeader() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .weight(1F)
                    .size(32.dp)
                    .clickableWithoutRipple { /* Todo: */ },
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "swipeLeft"
            )
            Text(
                modifier = Modifier.weight(5F),
                text = "Eylül",
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
            Image(
                modifier = Modifier
                    .weight(1F)
                    .size(32.dp)
                    .clickableWithoutRipple { /* Todo: */ },
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "swipeRight"
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            val weekDays = listOf("Pzt", "Salı", "Çarş", "Perş", "Cuma", "Cmt", "Pzr")
            for (day in weekDays)
                Text(
                    modifier = Modifier.weight(1f),
                    text = day,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 0.5.dp)
    }

}

@Composable
fun calMonth() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.size(8.dp))
        calHeader()
        Spacer(modifier = Modifier.size(8.dp))
        calWeeks(weekNumbers = listOf(29, 30, 1, 2, 3, 4, 5))
        calWeeks(weekNumbers = listOf(6, 7, 8, 9, 10, 11, 12))
        calWeeks(weekNumbers = listOf(13, 14, 15, 16, 17, 18, 19))
        calWeeks(weekNumbers = listOf(20, 21, 22, 23, 24, 25, 26))
        calWeeks(weekNumbers = listOf(27, 28, 29, 30, 31, 1, 2))
    }
}

@Composable
fun calWeeks(weekNumbers: List<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var selectedDate by remember { mutableStateOf(-1)}
        var unselectedDate by remember { mutableStateOf(-1)}

        for (dayNumber in weekNumbers) {
            var isSelected by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickableWithoutRipple {
                        if (selectedDate != -1)
                            unselectedDate = dayNumber
                        isSelected = isSelected.not()
                        if (isSelected)
                            selectedDate = dayNumber
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = dayNumber.toString(),
                    color = markToday(dayNumber),
                    style = MaterialTheme.typography.body1
                )
                if (isSelected)
                    SelectDate()
                if (unselectedDate != -1 && isSelected.not()) {
                    UnselectDate()
                    unselectedDate = -1
                }
                if (dayNumber % 5 == 0)
                    Column(
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(5.dp)) {
                        for (j in listOf(1,2,3))
                            MarkTheDate(color = secondary)
                    }
            }
        }
    }
}

@Composable
fun SelectDate() {
    val animateFloat = remember { Animatable(0f) }
    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing)
        )
    }
    Canvas(
        modifier = Modifier.size(26.dp)
    ) {
        drawArc(
            color = secondary,
            startAngle = 0f,
            sweepAngle = 360f * animateFloat.value,
            useCenter = false,
            style = Stroke(width = 1.5f.dp.toPx())
        )
    }
}

@Composable
fun UnselectDate() {
    val animateFloat = remember { Animatable(1f) }
    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing)
        )
    }
    Canvas(
        modifier = Modifier.size(26.dp)
    ) {
        drawArc(
            color = secondary,
            startAngle = 0f,
            sweepAngle = 360f * animateFloat.value,
            useCenter = false,
            style = Stroke(width = 1.5f.dp.toPx())
        )
    }
}

@Composable
fun MarkTheDate(color: Color) {
    Canvas(
        Modifier
            .size(6.dp)
            .padding(1.dp)) {
        drawCircle(color = color)
    }
}

fun markToday(day: Int): Color {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd")
    //val today = LocalDate.parse(LocalDate.now().toString(), dateFormatter)
    //val selected = LocalDate.parse(day.toString(), dateFormatter)
    val now = LocalDate.now()

    val maxDaysInMonth = LocalDateTime.MAX

    return if (now.dayOfMonth == day) secondaryLight else Color.White
}

@Preview
@Composable
fun PreviewMyCalendar() {
    MyCalendar()
}