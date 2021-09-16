package com.enesky.evimiss

import android.app.Application
import android.os.Build
import com.jakewharton.threetenabp.AndroidThreeTen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

class App: Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mFirebaseCrashlytics: FirebaseCrashlytics? = null
    private var mFirebaseAuth: FirebaseAuth? = null

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        val tempUserId = Build.BRAND + " " + Build.DEVICE
        mFirebaseAnalytics?.setUserId(tempUserId)
        mFirebaseCrashlytics?.setUserId(tempUserId)
    }

}