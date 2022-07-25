package com.eky.evimiss.ui.custom.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eky.evimiss.ui.theme.primaryLight
import com.eky.evimiss.ui.theme.secondary
import com.eky.evimiss.ui.theme.secondaryDark
import com.eky.evimiss.ui.theme.secondaryLight

@Composable
fun MyChart(modifier: Modifier) {
    Main(modifier)
}

@Composable
fun Main(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(2.5f).aspectRatio(1f)
        ) {
            Canvas(modifier = Modifier.align(Alignment.Center).fillMaxSize().padding(32.dp)) {
                drawArc(
                    color = primaryLight,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 32f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = secondaryLight,
                    startAngle = 0f,
                    sweepAngle = 30f,
                    useCenter = false,
                    style = Stroke(width = 56f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = secondaryDark,
                    startAngle = 30f,
                    sweepAngle = 60f,
                    useCenter = false,
                    style = Stroke(width = 56f, cap = StrokeCap.Round)
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "1450,55 TL",
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
        //Categories
        Column(modifier = Modifier.weight(1f)) {
            for (i in listOf(1,2,3,4)) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Canvas(
                        Modifier
                            .size(4.dp)
                            .align(Alignment.CenterVertically)) {
                        drawCircle(color = secondary)
                    }
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(com.eky.evimiss.R.string.label_expense_type),
                        color = Color.White,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Composable
fun drawArc(drawScope: DrawScope, newExpense: Float, expenseType: ExpenseType) {
    val income = 36
    with(drawScope) {
        drawArc(
            color = expenseType.color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 45f)
        )
    }
}

@Preview
@Composable
fun PreviewMyChart() {
    Main(modifier = Modifier.fillMaxWidth())
}