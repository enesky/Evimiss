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
import com.enesky.evimiss.utils.*
import org.threeten.bp.LocalDate

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@Composable
fun MyCalendar() {
    CalMonth()
}

@Composable
fun CalHeader() {
    var currentDate by remember { mutableStateOf(MyDate(date = getToday())) }
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
                    .clickableWithoutRipple {
                        currentDate = MyDate(date = currentDate.date.minusMonths(1))
                    },
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "swipeLeft"
            )
            Text(
                modifier = Modifier.weight(5F),
                text = currentDate.month,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
            Image(
                modifier = Modifier
                    .weight(1F)
                    .size(32.dp)
                    .clickableWithoutRipple {
                        currentDate = MyDate(date = currentDate.date.plusMonths(1))
                    },
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "swipeRight"
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            for (day in getDaynames())
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
fun CalMonth() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.size(8.dp))
        CalHeader()
        Spacer(modifier = Modifier.size(8.dp))
        CalWeeks(monthList = getMonthList())
    }
}

@Composable
fun CalWeeks(monthList: MutableList<MyDate>) {

    val weeks = monthList.getWeeksOfMonth()

    for (weekList in weeks) {
        CalWeek(week = weekList.second)
    }

}

@Composable
fun CalWeek(week: MutableList<MyDate>) {

    var selectedDate: LocalDate? by remember { mutableStateOf(null) }
    var unselectedDate: LocalDate? by remember { mutableStateOf(null) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        for (day in week) {
            var isSelected by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickableWithoutRipple {
                        if (day.date.isFromThisMonth().not())
                            return@clickableWithoutRipple
                        if (selectedDate != null)
                            unselectedDate = day.date
                        isSelected = isSelected.not()
                        if (isSelected)
                            selectedDate = day.date
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = day.dayOfMonth.toString(),
                    color = colorizeDate(myDate = day),
                    style = MaterialTheme.typography.body1
                )
                if (isSelected)
                    SelectDate()
                if (unselectedDate != null && isSelected.not()) {
                    UnselectDate()
                    unselectedDate = null
                }
                if (day.hasEvents)
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
            animationSpec = tween(durationMillis = 250, easing = LinearEasing)
        )
    }
    Canvas(modifier = Modifier.size(26.dp)) {
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
    val animateFloat = remember { Animatable(0f) }
    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 250, easing = LinearEasing)
        )
    }
    Canvas(modifier = Modifier.size(26.dp)) {
        drawArc(
            color = secondary,
            startAngle = 360f * animateFloat.value,
            sweepAngle = 0f,
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

fun colorizeDate(myDate: MyDate): Color = when {
    isToday(myDate.date) -> secondaryLight
    myDate.date.isFromThisMonth().not() -> Color.White.copy(alpha = 0.3f)
    else -> Color.White
}

@Preview
@Composable
fun PreviewMyCalendar() {
    MyCalendar()
}