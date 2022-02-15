package com.enesky.evimiss.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.enesky.evimiss.App
import com.enesky.evimiss.R
import com.enesky.evimiss.data.repo.CalendarDataSource
import com.enesky.evimiss.utils.PermissionsUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionsUtil: PermissionsUtil

    @Inject
    lateinit var calendarDataSource: CalendarDataSource

    private var isUserAvailable: Boolean? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var credentialListener: ((credential: AuthCredential) -> Unit)? = null

    override fun onStart() {
        super.onStart()
        val currentUser = App.mAuth?.currentUser
        isUserAvailable = currentUser != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationFromSplash(isUserAvailable == true)
            if (isUserAvailable == true)
                permissionsUtil.RequestCalendarPermissions {
                    calendarDataSource.init()
                }
        }
        registerForGoogleSignIn()
    }

    private fun registerForGoogleSignIn() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    credentialListener?.invoke(credential)
                } catch (e: ApiException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    fun signInWithGoogle(listener: (credential: AuthCredential) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        credentialListener = listener
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        resultLauncher?.launch(googleSignInClient?.signInIntent)
    }

}