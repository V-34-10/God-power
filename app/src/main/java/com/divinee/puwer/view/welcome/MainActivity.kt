package com.divinee.puwer.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityMainBinding
import com.divinee.puwer.view.daily.DailyActivity
import com.divinee.puwer.view.daily.DailyRewardManager
import com.divinee.puwer.view.menu.MenuActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var dailyRewardManager: DailyRewardManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dailyRewardManager = DailyRewardManager(this)
        binding.buttonStart.setOnClickListener {
            if (dailyRewardManager.shouldShowDailyActivity(this)) {
                it.startAnimation(startAnimation(this))
                startActivity(Intent(this@MainActivity, DailyActivity::class.java))

                dailyRewardManager.saveLastDailyActivityShowDate(this)
            } else {
                startActivity(Intent(this@MainActivity, MenuActivity::class.java))
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() = super.onBackPressed()
}