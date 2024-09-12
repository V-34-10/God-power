package com.divinee.puwer.app

import android.app.Application
import com.appodeal.ads.Appodeal

class App : Application() {
    private val applicationID = "29c19453396a950cad4f196a1239e03a595c2794f3148775"
    override fun onCreate() {
        super.onCreate()
        Appodeal.initialize(this, applicationID, Appodeal.INTERSTITIAL)
    }
}