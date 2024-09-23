package com.divinee.puwer.view.settings

import android.content.Context
import androidx.annotation.RawRes

class MusicSetup(private val context: Context) {
    private val soundPlayers = mutableMapOf<Int, SoundPlayer>()
    fun playSound(@RawRes soundResId: Int, loop: Boolean = false) {
        soundPlayers.getOrPut(soundResId) {
            SoundPlayer(context)
        }.play(soundResId, loop)
    }

    fun pause() = soundPlayers.forEach { (_, player) -> player.pause() }
    fun resume() = soundPlayers.forEach { (_, player) -> player.resume() }
    fun release() {
        soundPlayers.forEach { (_, player) -> player.release() }
        soundPlayers.clear()
    }
}