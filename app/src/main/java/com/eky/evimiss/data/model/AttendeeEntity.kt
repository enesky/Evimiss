package com.eky.evimiss.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Enes Kamil YILMAZ on 11/02/2022
 */

@Parcelize
data class AttendeeEntity (
    var eventID: String? = "",
    var name: String? = "",
    var email: String? = "",
) : Parcelable
