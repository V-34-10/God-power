package com.divinee.puwer.view.privacy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityPrivacyBinding
import com.divinee.puwer.view.daily.DailyActivity

class PrivacyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setAgreePrivacy()
        runIntentPrivacy()
    }

    private fun setSharedPreferencesAccept() {
        return this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
            .putBoolean("PrivacyActivity", true)
            .apply()
    }

    private fun setAgreePrivacy() {
        binding.apply {
            btnAccept.setOnClickListener {
                it.startAnimation(startAnimation(this@PrivacyActivity))
                setSharedPreferencesAccept()
                startActivity(Intent(this@PrivacyActivity, DailyActivity::class.java))
                finish()
            }
        }
    }

    private fun runIntentPrivacy() {
        binding.apply {
            privacyLink.setOnClickListener {
                it.startAnimation(startAnimation(this@PrivacyActivity))
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.go_link_privacy))
                    )
                )
            }
        }
    }

    @Deprecated(
        "Deprecated in Java"
    )
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}