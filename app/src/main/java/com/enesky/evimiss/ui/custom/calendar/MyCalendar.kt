package com.enesky.evimiss.ui.custom.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
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
import org.threeten.bp.Month

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@ExperimentalAnimationApi
@Composable
fun MyCalendar() {
    Main()
}

@ExperimentalAnimationApi
@Composable
fun Main() {

    val viewModel = MyCalendarVM()
    val viewState = viewModel.myCalendarViewState().collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(4.dp)
    ) {
        item {
            CalHeader(viewModel, viewState)
        }
        item {
            Spacer(modifier = Modifier.size(4.dp))
            CalWeek(viewModel, viewState)
        }
        item {
            Spacer(modifier = Modifier.size(4.dp))

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 0.5.dp)
        }
        item {
            Spacer(modifier = Modifier.size(4.dp))
            DetailTheDate(viewModel, viewState)
        }
    }

}

@Composable
fun CalHeader(viewModel: MyCalendarVM, viewState: State<MyCalendarVM.MyCalendarViewState>) {
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
                    .clickable {
                        viewModel.onPreviousMonthClicked()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "swipeLeft"
            )
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(5F),
                text = viewState.value.title,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
            Image(
                modifier = Modifier
                    .weight(1F)
                    .size(32.dp)
                    .clickableWithoutRipple {
                        viewModel.onNextMonthClicked()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "swipeRight"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (day in getDayNames())
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
fun CalWeek(viewModel: MyCalendarVM, viewState: State<MyCalendarVM.MyCalendarViewState>) {

    val weekLists = viewState.value.weekLists

    for (weekList in weekLists) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (myDate in weekList.second) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            viewModel.onDateSelected(selectedDate = myDate)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = myDate.dayOfMonth.toString(),
                        color = colorizeDate(
                            myDate = myDate,
                            givenMonth = viewState.value.currentDate.date.month
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    when {
                        myDate.date.isEqual(getToday()) && viewModel.isSelectedDateInitial() -> SelectDate()
                        viewState.value.selectedDate.date.isEqual(myDate.date) -> SelectDate()
                    }
                    if (myDate.hasEvents) {
                        Column(
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(5.dp)
                        ) {
                            for (j in myDate.events)
                                MarkTheDate(color = secondary)
                        }
                    }
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
    Canvas(modifier = Modifier.size(28.dp)) {
        drawArc(
            color = secondary,
            startAngle = 0f,
            sweepAngle = 360f * animateFloat.value,
            useCenter = false,
            style = Stroke(width = 1.25f.dp.toPx())
        )
    }
}

@Composable
fun MarkTheDate(color: Color) {
    Canvas(
        Modifier
            .size(6.dp)
            .padding(1.dp)
    ) {
        drawCircle(color = color)
    }
}

@ExperimentalAnimationApi
@Composable
fun DetailTheDate(viewModel: MyCalendarVM, viewState: State<MyCalendarVM.MyCalendarViewState>) {
    val selectedDate = viewState.value.selectedDate
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxHeight(),
                text = selectedDate.date.convert2DetailedDate(),
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            AnimatedVisibility(visible = selectedDate.date.isEqual(getToday()).not()) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickableWithoutRipple {
                            viewModel.onBackToTodayClicked()
                        },
                    painter = painterResource(id = R.drawable.ic_today),
                    contentDescription = "today",
                    tint = secondaryLight
                )
            }
        }
    }
}

fun colorizeDate(myDate: MyDate, givenMonth: Month): Color = when {
    isToday(myDate.date) -> secondaryLight
    myDate.date.isFromThisMonth(givenMonth).not() -> Color.White.copy(alpha = 0.3f)
    else -> Color.White
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewMyCalendar() {
    Main()
}