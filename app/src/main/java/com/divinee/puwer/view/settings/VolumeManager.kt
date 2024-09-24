package com.divinee.puwer.view.settings

import android.content.Context
import android.media.AudioManager

class VolumeManager(context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var savedVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun setVolume(on: Boolean) {
        if (on) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0)
        } else {
            savedVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }
    }
}