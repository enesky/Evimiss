package com.eky.evimiss.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEntity(
    val id: String,
    val accountName: String,
    val name: String,
    val ownerAccount: String
): Parcelable {

    fun isMain() = ownerAccount == name

}
