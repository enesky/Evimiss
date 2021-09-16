package com.enesky.evimiss.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.enesky.evimiss.App

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private var isUserAvailable: Boolean = false

    override fun onStart() {
        super.onStart()
        val currentUser = (application as App).mFirebaseAuth?.currentUser
        isUserAvailable = currentUser != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationFromSplash(isUserAvailable)
        }
    }

}