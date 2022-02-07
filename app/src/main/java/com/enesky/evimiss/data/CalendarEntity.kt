package com.enesky.evimiss.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEntity(
    val id: String,
    val isMain: Boolean = false
): Parcelable
