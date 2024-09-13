package com.divinee.puwer.view.daily

import android.content.Context
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailyRewardManager(context: Context) {

    private val prefs = context.getSharedPreferences("daily_rewards", Context.MODE_PRIVATE)
    private val rewards = listOf(200, 500, 1000)

    fun getRewardForDay(day: Int): Int {
        if (day in 1..rewards.size) {
            return rewards[day - 1]
        }
        return 0
    }

    fun isRewardClaimedForDay(day: Int): Boolean {
        return prefs.getBoolean("reward_claimed_day_$day", false)
    }

    fun setRewardClaimedForDay(day: Int) {
        prefs.edit().putBoolean("reward_claimed_day_$day", true).apply()
    }

    private fun getLastRewardDay(): Int {
        return prefs.getInt("last_reward_day", 0)
    }

    fun setLastRewardDay(day: Int) {
        prefs.edit().putInt("last_reward_day", day).apply()
    }

    fun canClaimReward(): Boolean {
        val lastRewardDay = getLastRewardDay()
        val currentDay = getCurrentDay()
        return currentDay > lastRewardDay
    }

    private fun getLastRewardDate(): Long {
        return prefs.getLong("lastRewardDate", 0)
    }

    fun setLastRewardDate(date: Long) {
        prefs.edit().putLong("lastRewardDate", date).apply()
    }

    fun getCurrentDay(): Int {
        val lastLaunchDate = prefs.getLong("lastLaunchDate", 0)
        val currentDate = System.currentTimeMillis()

        if (lastLaunchDate == 0L) {
            prefs.edit().putLong("lastLaunchDate", currentDate).apply()
            return 1
        }

        val diffInMillis = currentDate - lastLaunchDate
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        var currentDay = (getLastRewardDay() + diffInDays + 1).toInt()
        currentDay %= 3
        if (currentDay == 0) {
            currentDay = 3
        }
        return currentDay
    }

    fun checkAndResetProgress() {
        val lastRewardDate = getLastRewardDate()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = lastRewardDate

        if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastRewardDate) > 1 || allRewardsClaimed()) {
            resetRewardProgress()
        }
    }

    private fun resetRewardProgress() {
        setLastRewardDay(0)
        for (day in 1..rewards.size) {
            prefs.edit().remove("reward_claimed_day_$day").apply()
        }
    }

    private fun allRewardsClaimed(): Boolean {
        for (day in 1..rewards.size) {
            if (!isRewardClaimedForDay(day)) {
                return false
            }
        }
        return true
    }

    fun shouldShowDailyActivity(context: Context): Boolean {
        val prefs = context.getSharedPreferences("daily_activity_prefs", Context.MODE_PRIVATE)
        val lastShowDate = prefs.getLong("last_show_date", 0)

        val currentDate = Calendar.getInstance().timeInMillis
        return (currentDate - lastShowDate) >= TimeUnit.DAYS.toMillis(1)
    }

    fun saveLastDailyActivityShowDate(context: Context) {
        val prefs = context.getSharedPreferences("daily_activity_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_show_date", Calendar.getInstance().timeInMillis).apply()
    }
}