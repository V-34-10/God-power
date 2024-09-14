package com.divinee.puwer.view.daily

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.databinding.ActivityDailyBinding
import com.divinee.puwer.view.menu.MenuActivity

class DailyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDailyBinding.inflate(layoutInflater) }
    private lateinit var dailyRewardManager: DailyRewardManager
    private lateinit var btnFirstDay: ImageView
    private lateinit var btnSecondDay: ImageView
    private lateinit var btnThreeDay: ImageView
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
        dailyRewardManager.checkAndResetProgress()

        btnFirstDay = findViewById(R.id.btn_first_day)
        btnSecondDay = findViewById(R.id.btn_second_day)
        btnThreeDay = findViewById(R.id.btn_three_day)

        updateButtonStates()

        btnFirstDay.setOnClickListener {
            claimReward(1)
        }

        btnSecondDay.setOnClickListener {
            claimReward(2)
        }

        btnThreeDay.setOnClickListener {
            claimReward(3)
        }
    }

    private fun updateButtonStates() {
        val currentDay = dailyRewardManager.getCurrentDay()

        btnFirstDay.isEnabled = currentDay >= 1 && !dailyRewardManager.isRewardClaimedForDay(1)
        btnSecondDay.isEnabled = currentDay >= 2 && !dailyRewardManager.isRewardClaimedForDay(2)
        btnThreeDay.isEnabled = currentDay >= 3 && !dailyRewardManager.isRewardClaimedForDay(3)

        if (dailyRewardManager.isRewardClaimedForDay(1)) {
            btnFirstDay.setBackgroundResource(R.drawable.first_day_done_btn)
            btnFirstDay.isEnabled = false
            startActivity(Intent(this@DailyActivity, MenuActivity::class.java))
            finish()
        }

        if (dailyRewardManager.isRewardClaimedForDay(2)) {
            btnSecondDay.alpha = 1f
            btnSecondDay.isEnabled = true
            btnSecondDay.setBackgroundResource(R.drawable.second_day_done_btn)
            startActivity(Intent(this@DailyActivity, MenuActivity::class.java))
            finish()
        }

        if (dailyRewardManager.isRewardClaimedForDay(3)) {
            btnThreeDay.alpha = 1f
            btnThreeDay.isEnabled = true
            btnThreeDay.setBackgroundResource(R.drawable.three_day_done_btn)
            startActivity(Intent(this@DailyActivity, MenuActivity::class.java))
            finish()
        }
    }

    private fun claimReward(day: Int) {
        dailyRewardManager.setRewardClaimedForDay(day)
        dailyRewardManager.setLastRewardDay(day)
        dailyRewardManager.setLastRewardDate(System.currentTimeMillis())
        updateButtonStates()

        val reward = dailyRewardManager.getRewardForDay(day)

        val prefs = getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
        val currentBalance = prefs.getString("balanceScore", "0")?.toIntOrNull() ?: 0

        val newBalance = currentBalance + reward
        prefs.edit()
            .putString("balanceScore", newBalance.toString())
            .apply()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() = super.onBackPressed()
}