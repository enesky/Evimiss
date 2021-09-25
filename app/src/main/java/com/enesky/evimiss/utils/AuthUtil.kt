package com.enesky.evimiss.utils

import android.app.Activity
import com.enesky.evimiss.App
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException

fun Activity.createUserWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: (() -> Unit)? = null,
    onFail: ((message: String) -> Unit)? = null
) {
    App.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            onSuccess?.invoke()
        } else {
            if (task.exception is FirebaseAuthUserCollisionException)
                signInWithEmailAndPassword(email, password, onSuccess, onFail)
            onFail?.invoke(task.exception?.message.toString())
        }
    }
}

fun Activity.signInWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: (() -> Unit)? = null,
    onFail: ((message: String) -> Unit)? = null
) {
    App.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
        if (task.isSuccessful)
            onSuccess?.invoke()
        else
            onFail?.invoke(task.exception?.message.toString())
    }
}

fun Activity.signInWithCredential(
    credential: AuthCredential,
    onSuccess: (() -> Unit)? = null,
    onFail: ((message: String) -> Unit)? = null
) {
    App.mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
        if (task.isSuccessful)
            onSuccess?.invoke()
        else
            onFail?.invoke(task.exception?.message.toString())
    }
}

fun Activity.signInAnonymously(
    onSuccess: (() -> Unit)? = null,
    onFail: ((message: String) -> Unit)? = null
) {
    App.mAuth.signInAnonymously().addOnCompleteListener(this) { task ->
        if (task.isSuccessful)
            onSuccess?.invoke()
        else
            onFail?.invoke(task.exception?.message.toString())
    }
}

fun Activity.forgotPassword(
    email: String,
    onSuccess: (() -> Unit)? = null,
    onFail: ((message: String) -> Unit)? = null
) {
    App.mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
        if (task.isSuccessful)
            onSuccess?.invoke()
        else
            onFail?.invoke(task.exception?.message.toString())
    }
}

fun getUserEmail() = App.mAuth.currentUser?.email

fun isAnonymous() = App.mAuth.currentUser?.isAnonymous

fun signOut() = App.mAuth.signOut()
