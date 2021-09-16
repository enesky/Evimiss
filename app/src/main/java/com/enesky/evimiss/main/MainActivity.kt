package com.enesky.evimiss.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.enesky.evimiss.App

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private var isUserAvailable: Boolean = false

    override fun onStart() {
        super.onStart()
        val currentUser = App.mAuth.currentUser
        isUserAvailable = currentUser != null
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationFromSplash(isUserAvailable)
        }
    }

}