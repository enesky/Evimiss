package com.enesky.evimiss.utils

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.enesky.evimiss.App
import com.enesky.evimiss.R
import com.enesky.evimiss.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Enes Kamil YILMAZ on 18/02/2022
 */

@ActivityScoped
class GoogleSignInUtil @Inject constructor(
    @ActivityContext private val context: Context
){

    private var activity = context as MainActivity
    private var googleSignInClient: GoogleSignInClient? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var credentialListener: ((credential: AuthCredential) -> Unit)? = null

    fun registerForGoogleSignIn() {
        resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    credentialListener?.invoke(credential)
                } catch (e: ApiException) {
                    App.mCrashlytics?.recordException(e)
                }
            }
        }
    }

    fun signInWithGoogle(listener: (credential: AuthCredential) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        credentialListener = listener
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
        resultLauncher?.launch(googleSignInClient?.signInIntent)
    }

}