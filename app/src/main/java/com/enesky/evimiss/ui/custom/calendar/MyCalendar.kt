package com.enesky.evimiss.ui.custom.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Main()
}

@Composable
fun Main() {
    val activity = LocalContext.current.activity
    val viewModel = MyCalendarVM(activity?.contentResolver)
    val viewState = viewModel.myCalendarViewState().collectAsState()
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        item {
            CalHeader(viewModel, viewState)
        }
        item {
            CalWeek(viewModel, viewState)
        }
        item {
            DateDetails(viewState)
        }
        items(items = viewState.value.eventList) { item ->
            EventItem(eventEntity = item)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalHeader(viewModel: MyCalendarVM, viewState: State<MyCalendarViewState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .weight(3F)
                    .padding(end = 32.dp)
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
                    text = viewState.value.currentDate.date.year.toString(),
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = viewState.value.title,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            Box(
                modifier = Modifier.weight(3f),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(start = 32.dp)
                        .size(32.dp)
                        .clickableWithoutRipple {
                            viewModel.onNextMonthClicked()
                        },
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "swipeRight"
                )
                Row(
                    modifier = Modifier
                        .offset(x = (-8).dp, y = 0.dp)
                        .clickableWithoutRipple {
                            viewModel.onBackToTodayClicked()
                        }
                ) { // Had to use this cause of kotlin import error. To see visit: https://stackoverflow.com/questions/67975569/why-cant-i-use-animatedvisibility-in-a-boxscope
                    AnimatedVisibility(visible = viewModel.isBack2TodayAvailable()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.ic_today),
                                contentDescription = "today",
                                tint = secondaryLight
                            )
                            Text(
                                text = stringResource(R.string.label_today),
                                textAlign = TextAlign.Center,
                                color = secondaryLight,
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
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
                givenDate = viewState.value.currentDate.date
            ),
            style = MaterialTheme.typography.body1
        )

        if (myDate.date.isEqual(getToday()) && viewModel.isSelectedDateInitial())
            SelectDate()
        if (viewState.value.selectedDate.date.isEqual(myDate.date))
            SelectDate()

        MarkTheDate(boxScope = this, viewState = viewState)
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
fun MarkTheDate(boxScope: BoxScope, viewState: State<MyCalendarViewState>) {
    if (viewState.value.eventList.isEmpty().not())
        Column(
            modifier = with(boxScope) {
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
            }
        ) {
            for (j in viewState.value.eventList)
                Canvas(Modifier.size(6.dp).padding(1.dp)) {
                    drawCircle(color = secondary)
                }
        }
}

@Composable
fun DateDetails(viewState: State<MyCalendarViewState>) {
    Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 0.5.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.fillMaxHeight(),
            text = viewState.value.selectedDate.date.convert2DetailedDate(),
            color = Color.White,
            style = MaterialTheme.typography.body1
        )
    }
    Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 0.5.dp)
}

fun colorizeDate(myDate: MyDate, givenDate: LocalDate): Color = when {
    isToday(myDate.date) -> secondaryLight
    myDate.date.isFromThisMonth(givenDate).not() -> Color.White.copy(alpha = 0.3f)
    else -> Color.White
}

@Preview
@Composable
fun PreviewMyCalendar() {
    Main()
}