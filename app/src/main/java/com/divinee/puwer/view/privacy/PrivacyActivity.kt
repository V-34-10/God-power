package com.divinee.puwer.view.privacy

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityPrivacyBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.view.daily.DailyActivity

class PrivacyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
        setAgreePrivacy()
        runIntentPrivacy()
    }

    private fun setSharedPreferencesAccept() {
        return this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
            .putBoolean("PrivacyActivity", true)
            .apply()
    }

    private fun setAgreePrivacy() {
        with(binding) {
            btnAccept.setOnClickListener { view ->
                performClickAction(view) {
                    setSharedPreferencesAccept()
                    launchActivity(DailyActivity::class.java)
                }
            }
        }
    }

    private fun runIntentPrivacy() {
        with(binding) {
            privacyLink.setOnClickListener { view ->
                performClickAction(view) {
                    openPrivacyLink(getString(R.string.go_link_privacy))
                }
            }
        }
    }

    private fun performClickAction(view: View, action: () -> Unit) {
        view.startAnimation(startAnimation(this))
        action()
    }

    private fun <T> launchActivity(activityClass: Class<T>) {
        startActivity(Intent(this, activityClass))
        finish()
    }

    private fun openPrivacyLink(link: String) =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    @Deprecated(
        "Deprecated in Java"
    )
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}