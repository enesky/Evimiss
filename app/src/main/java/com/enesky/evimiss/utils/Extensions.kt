package com.enesky.evimiss.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
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

fun MainActivity.restart() {
    val newIntent = intent
    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(newIntent)
}

private tailrec fun Context.getMainActivity(): MainActivity? = when (this) {
    is MainActivity -> this
    is ContextWrapper -> baseContext.getMainActivity()
    else -> null
}

val Context.activity: MainActivity?
    get() = getMainActivity()

fun Activity.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)
    startActivity(intent)
}

fun Context.showToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, message, duration).show()
}