package com.enesky.evimiss.utils

import android.app.Activity
import com.enesky.evimiss.App

fun Activity.createUserWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: (() -> Unit)? = null,
    onFail: (() -> Unit)? = null
) {
    App.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            val user = App.mAuth.currentUser
            onSuccess?.invoke()
        } else {
            onFail?.invoke()
        }
    }
}

fun Activity.signInWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: (() -> Unit)? = null,
    onFail: (() -> Unit)? = null
) {
    App.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            val user = App.mAuth.currentUser
            onSuccess?.invoke()
        } else {
            // If sign in fails, display a message to the user.
            onFail?.invoke()
        }
    }
}

fun signOut() {
    App.mAuth.signOut()
}