package com.divinee.puwer.view.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.animation.SplashBarAnimation.loadingAnim
import com.divinee.puwer.animation.SplashBarAnimation.returnProgressWidth
import com.divinee.puwer.databinding.ActivityMainBinding
import com.divinee.puwer.network.NetworkChecker.checkEthernetStatus
import com.divinee.puwer.view.daily.DailyActivity
import com.divinee.puwer.view.privacy.PrivacyActivity
import com.divinee.puwer.view.welcome.AppodealSetup.initBanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        switchRunBannerOrMenu()
    }

    private fun switchRunBannerOrMenu() {
        if (checkEthernetStatus(this)) {
            initBanner(this)
            binding.buttonStart.isEnabled = false
            binding.buttonStart.visibility = View.GONE
            setAnimationBarForBanner()
        } else {
            navigateNotUseBanner()
        }
    }

    private fun setAnimationBarForBanner() {
        binding.animationBarLoad.lineSplash.loadingAnim(maxWidth = this.returnProgressWidth())
        lifecycleScope.launch {
            delay(20000L)
        }
    }

    fun navigateNotUseBanner() {
        binding.buttonStart.isEnabled = true
        binding.buttonStart.visibility = View.VISIBLE
        findViewById<View>(R.id.animationBarLoad).visibility = View.GONE
        binding.buttonStart.setOnClickListener {
            it.startAnimation(startAnimation(this))
            checkNavigateToDaily()
        }
    }

    fun checkNavigateToDaily() {
        val navigateActivity = if (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getBoolean("PrivacyActivity", false)
        ) {
            DailyActivity::class.java
        } else {
            PrivacyActivity::class.java
        }
        startActivity(Intent(this@MainActivity, navigateActivity))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() = super.onBackPressed()
}