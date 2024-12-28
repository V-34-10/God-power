package com.divinee.puwer.view.settings

import android.content.Context
import android.media.AudioManager

class VolumeManager(context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var gameMusicVolume: Int = 10

    fun setVolume(on: Boolean) {
        if (on) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, gameMusicVolume, 0)
        } else {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        }
    }
}