package com.enesky.evimiss.utils

import android.content.Context
import com.enesky.evimiss.App
import com.enesky.evimiss.main.MainActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AuthUtil @Inject constructor(
    @ActivityContext private val context: Context
) {

    private var activity = context as MainActivity

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (() -> Unit)? = null,
        onFail: ((message: String) -> Unit)? = null
    ) {
        App.mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess?.invoke()
                signIn()
            } else {
                if (task.exception is FirebaseAuthUserCollisionException)
                    signInWithEmailAndPassword(email, password, onSuccess, onFail)
                onFail?.invoke(task.exception?.message.toString())
            }
        }
    }

    private fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (() -> Unit)? = null,
        onFail: ((message: String) -> Unit)? = null
    ) {
        App.mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess?.invoke()
                signIn()
            } else
                onFail?.invoke(task.exception?.message.toString())
        }
    }

    fun signInWithCredential(
        credential: AuthCredential,
        onSuccess: (() -> Unit)? = null,
        onFail: ((message: String) -> Unit)? = null
    ) {
        App.mAuth?.signInWithCredential(credential)?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess?.invoke()
                signIn()
            } else
                onFail?.invoke(task.exception?.message.toString())
        }
    }

    fun signInAnonymously(
        onSuccess: (() -> Unit)? = null,
        onFail: ((message: String) -> Unit)? = null
    ) {
        App.mAuth?.signInAnonymously()?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess?.invoke()
                signIn()
            } else
                onFail?.invoke(task.exception?.message.toString())
        }
    }

    fun forgotPassword(
        email: String,
        onSuccess: (() -> Unit)? = null,
        onFail: ((message: String) -> Unit)? = null
    ) {
        App.mAuth?.sendPasswordResetEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSuccess?.invoke()
            else
                onFail?.invoke(task.exception?.message.toString())
        }
    }

}

fun getUserEmail(): String? = App.mAuth?.currentUser?.email

fun isAnonymous(): Boolean = App.mAuth?.currentUser?.isAnonymous == true

fun signOut() {
    App.mAuth?.signOut()
    SharedPrefUtil.clearAll()
}

private fun signIn() {
    (getUserEmail() ?: App.mAuth?.uid).let {
        App.mAnalytics?.setUserId(it)
        App.mCrashlytics?.setUserId(it!!)
    }
}


