package com.divinee.puwer.view.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.animation.SplashBarAnimation.loadingAnim
import com.divinee.puwer.animation.SplashBarAnimation.returnProgressWidth
import com.divinee.puwer.databinding.ActivityMainBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.network.NetworkChecker.checkEthernetStatus
import com.divinee.puwer.view.daily.DailyActivity
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.privacy.PrivacyActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val apiRequestManager by lazy { ResponseToServer(this) }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
        switchRunBannerOrMenu()
    }

    private fun switchRunBannerOrMenu() {
        if (checkEthernetStatus(this)) {
            binding.buttonStart.isEnabled = false
            binding.buttonStart.visibility = View.GONE
            setAnimationBarForBanner()

            lifecycleScope.launch {
                makeApiRequest()
            }

        } else {
            navigateNotUseBanner()
        }
    }

    private fun makeApiRequest() {
        lifecycleScope.launch {
            val response = apiRequestManager.getGameOffers()
            if (response.isNotEmpty()) {
                val isDemoMode = response.any { offer ->
                    !offer.menuLabel.startsWith("https://")
                }

                if (isDemoMode) {
                    Log.d("ApiRequest", "Demo mode detected. menuLabel is not valid URL.")
                    checkNavigateToDaily()
                } else {
                    getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                        .putBoolean("StatusOffer", true).apply()
                    startActivity(
                        Intent(
                            this@MainActivity,
                            MenuActivity::class.java
                        ).putParcelableArrayListExtra("listGamesOffer", ArrayList(response))
                    )
                    Log.d("ApiRequest", "Parse Response: ${ArrayList(response)}")
                    finish()
                }
            } else {
                getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("StatusOffer", false).apply()
                lifecycleScope.launch {
                    delay(2000L)
                    checkNavigateToDaily()
                }
            }
        }
    }

    private fun setAnimationBarForBanner() {
        binding.animationBarLoad.lineSplash.loadingAnim(maxWidth = this.returnProgressWidth())
        lifecycleScope.launch {
            delay(15000L)
        }
    }

    private fun navigateNotUseBanner() {
        binding.buttonStart.isEnabled = true
        binding.buttonStart.visibility = View.VISIBLE
        findViewById<View>(R.id.animationBarLoad).visibility = View.GONE
        binding.buttonStart.setOnClickListener {
            it.startAnimation(startAnimation(this))
            checkNavigateToDaily()
        }
    }

    private fun checkNavigateToDaily() {
        val navigateActivity = when {
            isPrivacyAccepted() -> DailyActivity::class.java
            else -> PrivacyActivity::class.java
        }
        navigateToActivity(navigateActivity)
    }

    private fun navigateToActivity(activityClass: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activityClass))
        finish()
    }

    private fun isPrivacyAccepted(): Boolean =
        getSharedPreferences("PrefDivinePower", MODE_PRIVATE).getBoolean("PrivacyActivity", false)

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}