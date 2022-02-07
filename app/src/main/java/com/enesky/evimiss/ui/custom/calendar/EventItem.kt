package com.enesky.evimiss.ui.custom.calendar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enesky.evimiss.R
import com.enesky.evimiss.data.EventEntity
import com.enesky.evimiss.ui.theme.secondary
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun EventItem(eventEntity: EventEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Canvas(
            Modifier
                .size(4.dp)
                .align(Alignment.CenterVertically)
        ) {
            drawCircle(color = secondary)
        }
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            if (eventEntity.isAllDay == false) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = eventEntity.startDateTime.toString(),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )
                    if (eventEntity.endDateTime != null) {
                        Text(
                            text = " - ",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = eventEntity.endDateTime.toString(),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = eventEntity.title.toString(),
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = eventEntity.description.toString(),
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@Composable
@Preview
fun EventItemsPreview() {
    EventItem(
        eventEntity = EventEntity(
            title = stringResource(R.string.label_lorem_ipsum_title),
            description = stringResource(R.string.label_lorem_ipsum_desc),
            dtStart = null,
            dtEnd = null
        )
    )
}