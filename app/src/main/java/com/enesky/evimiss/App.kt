package com.enesky.evimiss

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.enesky.evimiss.utils.CalendarUtil
import com.jakewharton.threetenabp.AndroidThreeTen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

class App: Application() {

    companion object {
        private var instance: App? = null
        val mInstance: App?
            get() = instance

        private var firebaseAuth: FirebaseAuth? = null
        val mAuth: FirebaseAuth?
            get() = firebaseAuth

        private var firebaseAnalytics: FirebaseAnalytics? = null
        val mAnalytics: FirebaseAnalytics?
            get() = firebaseAnalytics

        private var firebaseCrashlytics: FirebaseCrashlytics? = null
        val mCrashlytics: FirebaseCrashlytics?
            get() = firebaseCrashlytics

        private var sharedPreferences: SharedPreferences? = null
        val mPrefs: SharedPreferences?
            get() = sharedPreferences

        private var calendarUtil: CalendarUtil? = null
        val mCalendarUtil: CalendarUtil?
            get() = calendarUtil
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(instance)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        calendarUtil = CalendarUtil()

        val tempUserId = Build.BRAND + " " + Build.DEVICE
        firebaseAnalytics?.setUserId(tempUserId)
        firebaseCrashlytics?.setUserId(tempUserId)
    }

}