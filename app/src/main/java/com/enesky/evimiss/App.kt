package com.enesky.evimiss

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.jakewharton.threetenabp.AndroidThreeTen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

class App: Application() {

    companion object {
        private lateinit var instance: App
        val mInstance: App
            get() = instance

        private lateinit var firebaseAuth: FirebaseAuth
        val mAuth: FirebaseAuth
            get() = firebaseAuth

        private lateinit var firebaseAnalytics: FirebaseAnalytics
        val mAnalytics: FirebaseAnalytics
            get() = firebaseAnalytics

        private lateinit var firebaseCrashlytics: FirebaseCrashlytics
        val mCrashlytics: FirebaseCrashlytics
            get() = firebaseCrashlytics

        private lateinit var sharedPreferences: SharedPreferences
        val mPrefs: SharedPreferences
            get() = sharedPreferences

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(instance)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        val tempUserId = Build.BRAND + " " + Build.DEVICE
        firebaseAnalytics.setUserId(tempUserId)
        firebaseCrashlytics.setUserId(tempUserId)
    }

}