package com.enesky.evimiss.utils

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.enesky.evimiss.App
import com.enesky.evimiss.main.MainActivity

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

@ExperimentalAnimationApi
fun MainActivity.restart() {
    finish()
    val newIntent = intent
    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(newIntent)
}