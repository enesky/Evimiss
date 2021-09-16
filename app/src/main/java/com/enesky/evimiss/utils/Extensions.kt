package com.enesky.evimiss.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.enesky.evimiss.App

/**
 * Created by Enes Kamil YILMAZ on 06/09/2021
 */

inline fun Modifier.clickableWithoutRipple(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun getString(@StringRes id : Int) = App.mInstance.getString(id)