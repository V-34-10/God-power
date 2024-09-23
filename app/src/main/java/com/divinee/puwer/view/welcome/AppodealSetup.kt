package com.divinee.puwer.view.welcome

import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks

object AppodealSetup {
    var statusBannerShow = false
    fun initBanner(activity: AppCompatActivity) =
        Appodeal.setInterstitialCallbacks(createInterstitialCallbacks(activity))

    private fun createInterstitialCallbacks(activity: AppCompatActivity): InterstitialCallbacks {
        return object : InterstitialCallbacks {
            override fun onInterstitialLoaded(isPrecache: Boolean) {
                if (!statusBannerShow) Appodeal.show(activity, Appodeal.INTERSTITIAL)
            }

            override fun onInterstitialFailedToLoad() = handleBannerFailure(activity)
            override fun onInterstitialShown() {
                statusBannerShow = true
            }

            override fun onInterstitialShowFailed() = handleBannerFailure(activity)
            override fun onInterstitialClosed() = (activity as MainActivity).checkNavigateToDaily()
            override fun onInterstitialClicked() {}
            override fun onInterstitialExpired() = Appodeal.cache(activity, Appodeal.INTERSTITIAL)
        }
    }

    private fun handleBannerFailure(activity: AppCompatActivity) {
        (activity as? MainActivity)?.navigateNotUseBanner()
    }
}