package com.enesky.evimiss.ui.screens.expense

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.enesky.evimiss.R
import com.enesky.evimiss.main.MainActivity
import com.enesky.evimiss.ui.custom.chart.MyChart
import com.enesky.evimiss.ui.screens.main.MainScaffold
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.utils.*

@Composable
fun ExpenseScreen() {

    val activity = LocalContext.current.activity

    var lazyColumnHeight = 0f
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }
    val constraintSet = ConstraintSet {
        val pieChart = createRefFor("chart")
        val expenses = createRefFor("expenses")
        val settings = createRefFor("settings")

        constrain(pieChart) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
        }

        constrain(expenses) {
            top.linkTo(pieChart.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            //bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }

        constrain(settings) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
    }
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .layoutId("settings")
                .padding(4.dp)
                .clickable {
                    //Todo: navigate to settings screen
                    signOut()
                    activity?.restart()
                },
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
            tint = secondary
        )
        MyChart(modifier = Modifier.layoutId("chart"))
        LazyColumn(
            modifier = Modifier
                .layoutId("expenses")
                .padding(horizontal = 16.dp)
                .drawBehind {
                    drawLine(
                        color = secondary,
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        start = Offset(6.dp.toPx(), 12.dp.toPx()),
                        end = Offset(6.dp.toPx(), lazyColumnHeight * animatedProgress.value),
                    )
                }
                .onGloballyPositioned {
                    lazyColumnHeight = it.size.toSize().height
                }
        ) {
            item {
                ExpenseDate()
            }
            item {
                ExpenseItem()
            }
            item {
                ExpenseItem()
            }
        }
    }
}

@Composable
fun ExpenseDate() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Canvas(
            Modifier
                .size(8.dp)
                .offset(x = 2.dp)) {
            drawCircle(color = secondary)
        }

        Text(
            modifier = Modifier
                .padding(4.dp)
                .offset(x = 4.dp),
            text = getToday().convert2TimelineDate(),
            color = Color.White,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun ExpenseItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .offset(x = 16.dp),
            text = stringResource(R.string.label_expense),
            color = Color.White,
            style = MaterialTheme.typography.subtitle2
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