package com.divinee.puwer.view.daily

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.divinee.puwer.R
import com.divinee.puwer.databinding.ActivityDailyBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.view.games.findcards.bonusgame.BonusWheelGame.stringToNumber
import com.divinee.puwer.view.menu.MenuActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDailyBinding.inflate(layoutInflater) }
    private lateinit var sharedPreferences: SharedPreferences
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)

        sharedPreferences = getSharedPreferences("PrefDivinePower", Context.MODE_PRIVATE)
        if (wasRewardShownToday()) {
            goToMenu()
            return
        }

        checkDailyReward()
        loadRewardProgress()
        dailyClickDaysButtons()
    }

    private fun dailyClickDaysButtons() {
        binding.btnFirstDay.setOnClickListener {
            collectReward(1, binding.btnFirstDay)
        }

        binding.btnSecondDay.setOnClickListener {
            collectReward(2, binding.btnSecondDay)
        }

        binding.btnThreeDay.setOnClickListener {
            collectReward(3, binding.btnThreeDay)
        }
    }

    private fun wasRewardShownToday(): Boolean =
        sharedPreferences.getString("lastRewardShownDate", null) == dateFormat.format(Date())

    private fun markRewardShownToday() {
        with(sharedPreferences.edit()) {
            putString(
                "lastRewardShownDate",
                dateFormat.format(Date())
            )
            apply()
        }
    }

    private fun checkDailyReward() {
        val lastRewardDay = sharedPreferences.getString("lastRewardDay", null)
        val currentDate = dateFormat.format(Date())

        if (lastRewardDay == null || lastRewardDay != currentDate) {
            val currentDay = sharedPreferences.getInt("currentDay", 1)
            if (currentDay > 3) {
                resetRewardProgress()
            } else {
                with(sharedPreferences.edit()) {
                    putString("lastRewardDay", currentDate)
                    apply()
                }
            }
        }
    }

    private fun resetRewardProgress() {
        with(sharedPreferences.edit()) {
            putInt("currentDay", 1)
            putString("lastRewardDay", dateFormat.format(Date()))
            putBoolean("day1Rewarded", false)
            putBoolean("day2Rewarded", false)
            putBoolean("day3Rewarded", false)
            commit()
        }
    }

    private fun loadRewardProgress() {
        val currentDay = sharedPreferences.getInt("currentDay", 1)
        if (currentDay > 3) {
            resetRewardProgress()
        } else {
            updateRewardUI(currentDay)
        }
    }

    private fun updateRewardUI(currentDay: Int) {
        updateButtonState(binding.btnFirstDay, 1, currentDay)
        updateButtonState(binding.btnSecondDay, 2, currentDay)
        updateButtonState(binding.btnThreeDay, 3, currentDay)
    }

    private fun updateButtonState(button: View, day: Int, currentDay: Int) {
        val isRewarded = sharedPreferences.getBoolean("day${day}Rewarded", false)
        button.isEnabled = day == currentDay && !isRewarded
        button.alpha = if (day <= currentDay) 1f else 0.5f
        Log.d("DailyActivity", "Day $day isRewarded: $isRewarded")
        if (isRewarded) {
            when (day) {
                1 -> button.setBackgroundResource(R.drawable.first_day_done_btn)
                2 -> button.setBackgroundResource(R.drawable.second_day_done_btn)
                3 -> button.setBackgroundResource(R.drawable.three_day_done_btn)
            }
        } else {
            when (day) {
                1 -> button.setBackgroundResource(R.drawable.first_day_btn)
                2 -> button.setBackgroundResource(R.drawable.second_day_btn)
                3 -> button.setBackgroundResource(R.drawable.three_day_btn)
            }
        }
    }

    private fun collectReward(day: Int, button: View) {
        val rewardAmount = when (day) {
            1 -> 200
            2 -> 500
            3 -> 1000
            else -> 0
        }

        val currentBalance = sharedPreferences.getString(
            "balanceScores",
            this.getString(R.string.text_default_balance)
        )
        var balance = stringToNumber(currentBalance.toString())
        balance += rewardAmount
        with(sharedPreferences.edit()) {
            putString("balanceScores", balance.toString())
            putBoolean("day${day}Rewarded", true)
            apply()
        }
        markRewardShownToday()
        updateButtonState(button, day, day)

        val nextDay = day + 1
        if (nextDay <= 3) {
            with(sharedPreferences.edit()) {
                putInt("currentDay", nextDay)
                apply()
            }
        } else {
            with(sharedPreferences.edit()) {
                putInt("currentDay", 1)
                apply()
            }
            resetRewardProgress()
        }

        goToMenu()
    }

    private fun goToMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        goToMenu()
    }
}