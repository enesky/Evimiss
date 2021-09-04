package com.enesky.evimiss.ui.custom.calendar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enesky.evimiss.ui.theme.secondary

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@Composable
fun MyCalendar() {
    calMonth()
}

@Composable
fun calMonth() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val weekNumbers1  = listOf(29,30,1,2,3,4,5)
        val weekNumbers2  = listOf(6,7,8,9,10,11,12)
        val weekNumbers3  = listOf(13,14,15,16,17,18,19)
        val weekNumbers4  = listOf(20,21,22,23,24,25,26)
        val weekNumbers5  = listOf(27,28,29,30,31,1,2)
        calWeeks(weekNumbers = weekNumbers1)
        calWeeks(weekNumbers = weekNumbers2)
        calWeeks(weekNumbers = weekNumbers3)
        calWeeks(weekNumbers = weekNumbers4)
        calWeeks(weekNumbers = weekNumbers5)
    }
}

@Composable
fun calWeeks(weekNumbers: List<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in weekNumbers) {
            var isSelected by remember { mutableStateOf(false)}
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            role = Role.Button
                        ) {
                            isSelected = isSelected.not()
                        }
                        .padding(8.dp),
                    text = i.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    )
                if (isSelected) {
                    CircularProgressIndicator(
                        progress = 1f,
                        color = secondary,
                        strokeWidth = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun drawCircle() {
    val radius = 200f
    val animateFloat = remember { Animatable(0f) }
    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000, easing = LinearEasing))
    }

    Canvas(modifier = Modifier.fillMaxSize()){
        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 360f * animateFloat.value,
            useCenter = false,
            topLeft = Offset(size.width / 4, size.height / 4),
            size = Size(radius * 2 , radius * 2),
            style = Stroke(2.0f)
        )
    }
}

@Preview
@Composable
fun PreviewMyCalendar() {
    MyCalendar()
}