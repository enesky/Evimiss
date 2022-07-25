package com.eky.evimiss.ui.custom.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eky.evimiss.R
import com.eky.evimiss.data.model.MyDate
import com.eky.evimiss.ui.theme.secondary
import com.eky.evimiss.ui.theme.secondaryLight
import com.eky.evimiss.utils.*
import org.threeten.bp.LocalDate

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@Composable
fun MyCalendar() {
    val viewModel: MyCalendarVM = hiltViewModel()
    val viewState = viewModel.myCalendarViewState().collectAsState()
    val listState = rememberLazyListState()

    Column {
        CalHeader(viewModel, viewState)
        CalWeek(viewModel, viewState)
        DateDetails(viewModel, viewState)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(items = viewState.value.eventMap[viewState.value.selectedDate.date] ?: mutableListOf()) { eventEntity ->
                EventItem(eventEntity)
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp))
    }
}

@Composable
fun CalHeader(viewModel: MyCalendarVM, viewState: State<MyCalendarViewState>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .weight(3F)
                    .size(32.dp)
                    .clickableWithoutRipple {
                        viewModel.onPreviousMonthClicked()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "swipeLeft"
            )
            Column(
                modifier = Modifier.weight(2F),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = viewState.value.todaysDate.date.year.toString(),
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = viewState.value.calendarTitle,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            Image(
                modifier = Modifier
                    .weight(3F)
                    .size(32.dp)
                    .clickableWithoutRipple {
                        viewModel.onNextMonthClicked()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "swipeRight"
               )
        }
        Spacer(modifier = Modifier.size(4.dp))
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
fun CalWeek(viewModel: MyCalendarVM, viewState: State<MyCalendarViewState>) {
    val weekLists = viewState.value.weekLists
    for (weekList in weekLists) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (myDate in weekList.second)
                CalDay(this, myDate, viewModel, viewState)
        }
    }
}

@Composable
fun CalDay(
    rowScope: RowScope,
    myDate: MyDate,
    viewModel: MyCalendarVM,
    viewState: State<MyCalendarViewState>
) {
    Box(
        modifier = with(rowScope) {
            Modifier
                .weight(1f)
                .clickableWithoutRipple {
                    viewModel.onDateSelected(selectedDate = myDate)
                }
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = myDate.dayOfMonth.toString(),
            color = colorizeDate(
                myDate = myDate,
                givenDate = viewState.value.todaysDate.date
            ),
            style = MaterialTheme.typography.body1
        )
        if ((myDate.date.isToday() && viewState.value.selectedDate.date.isToday()) || viewState.value.selectedDate.date.isEqual(myDate.date))
            SelectDate()
        if (myDate.hasEvents)
            MarkTheDate(boxScope = this, myDate = myDate)
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
fun MarkTheDate(boxScope: BoxScope, myDate: MyDate) {
    Column(
        modifier = with(boxScope) {
            Modifier
                .align(Alignment.TopEnd)
                .padding(5.dp)
        }
    ) {
        for (i in 1..myDate.events.take(3).size)
            Canvas(
                Modifier
                    .size(6.dp)
                    .padding(1.dp)) {
                drawCircle(color = secondary)
            }
    }
}

@Composable
fun DateDetails(viewModel: MyCalendarVM, viewState: State<MyCalendarViewState>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), color = Color.White, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewState.value.selectedDate.date.convert2DetailedDate(),
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            TodayButton(viewModel = viewModel)
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 0.5.dp)
    }
}

@Composable
fun TodayButton(viewModel: MyCalendarVM) {
    AnimatedVisibility(visible = viewModel.isBack2TodayAvailable()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickableWithoutRipple { viewModel.onBackToTodayClicked() }
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp),
                painter = painterResource(id = R.drawable.ic_today),
                contentDescription = "today",
                tint = secondaryLight
            )
            Text(
                text = stringResource(R.string.label_today),
                textAlign = TextAlign.Center,
                color = secondaryLight,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

private fun colorizeDate(myDate: MyDate, givenDate: LocalDate): Color = when {
    myDate.date.isToday() -> secondaryLight
    myDate.date.isFromThisMonth(givenDate).not() -> Color.White.copy(alpha = 0.3f)
    else -> Color.White
}

@Preview
@Composable
fun PreviewMyCalendar() {
    MyCalendar()
}