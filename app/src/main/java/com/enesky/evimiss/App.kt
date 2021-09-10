package com.enesky.evimiss

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.google.firebase.analytics.FirebaseAnalytics




/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

class App: Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

}