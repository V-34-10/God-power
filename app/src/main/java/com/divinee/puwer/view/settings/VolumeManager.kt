package com.divinee.puwer.view.settings

import android.content.Context
import android.media.AudioManager

class VolumeManager(context: Context) {
    private val managerMusic by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var defaultMusicVolume: Int = 10
    fun setOnVolume() =
        managerMusic.setStreamVolume(AudioManager.STREAM_MUSIC, defaultMusicVolume, 0)

    fun setOffVolume() {
        defaultMusicVolume = managerMusic.getStreamVolume(AudioManager.STREAM_MUSIC)
        managerMusic.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }
}