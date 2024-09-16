package com.divinee.puwer.view.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object MusicRunner {
    fun musicStartMode(context: Context, sourceMusic: Int, managerMusic: MusicSetup) {
        if (context.getSharedPreferences("PrefDivinePower", AppCompatActivity.MODE_PRIVATE)
                .getBoolean("musicStatus", false)
        ) managerMusic.apply { playSound(sourceMusic, true) }
    }

    fun soundStartMode(context: Context, sourceMusic: Int, managerMusic: MusicSetup) {
        if (context.getSharedPreferences("PrefDivinePower", AppCompatActivity.MODE_PRIVATE)
                .getBoolean("soundStatus", false)
        ) managerMusic.apply { playSound(sourceMusic, true) }
    }
}