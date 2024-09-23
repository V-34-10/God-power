package com.divinee.puwer.view.welcome

import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks

object AppodealSetup {
    private var statusBannerShow = false
    fun initBanner(activity: AppCompatActivity) {
        Appodeal.setInterstitialCallbacks(object : InterstitialCallbacks {
            override fun onInterstitialLoaded(isPrecache: Boolean) {
                if (!statusBannerShow) Appodeal.show(activity, Appodeal.INTERSTITIAL)
            }

            override fun onInterstitialFailedToLoad() =
                (activity as MainActivity).navigateNotUseBanner()

            override fun onInterstitialShown() {
                statusBannerShow = true
            }

            override fun onInterstitialShowFailed() =
                (activity as MainActivity).navigateNotUseBanner()

            override fun onInterstitialClosed() {
                statusBannerShow = false
                Appodeal.cache(activity, Appodeal.INTERSTITIAL)
            }

            override fun onInterstitialClicked() {}

            override fun onInterstitialExpired() = Appodeal.cache(activity, Appodeal.INTERSTITIAL)
        })
    }
}