package com.enesky.evimiss

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Created by Enes Kamil YILMAZ on 05/09/2021
 */

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}