package com.divinee.puwer.view.settings

import android.content.Context
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity

class MusicController(private val context: Context) {
    private val soundPlayers = mutableMapOf<Int, SoundPlayer>()

    fun playSound(@RawRes soundResId: Int, loop: Boolean = false) {
        val soundPlayer = soundPlayers.getOrPut(soundResId) {
            SoundPlayer(context)
        }
        soundPlayer.play(soundResId, loop)
    }

    fun pause() = soundPlayers.values.forEach { it.pause() }

    fun resume() = soundPlayers.values.forEach { it.resume() }

    fun release() {
        soundPlayers.values.forEach { it.release() }
        soundPlayers.clear()
    }
}

object MusicStart {
    fun musicStartMode(context: Context, sourceMusic: Int, managerMusic: MusicController) {
        if (context.getSharedPreferences("PrefDivinePower", AppCompatActivity.MODE_PRIVATE).getBoolean("musicStatus", false)) managerMusic.apply { playSound(sourceMusic, true) }
    }

    fun soundStartMode(context: Context, sourceMusic: Int, managerMusic: MusicController) {
        if (context.getSharedPreferences("PrefDivinePower", AppCompatActivity.MODE_PRIVATE).getBoolean("soundStatus", false)) managerMusic.apply { playSound(sourceMusic, true) }
    }
}