package com.divinee.puwer.view.settings

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class SoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    fun play(@RawRes soundResId: Int, loop: Boolean = false) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, soundResId).apply {
                isLooping = loop
                setOnCompletionListener {
                    if (!isLooping) release()
                }
                start()
            }
        } else {
            mediaPlayer?.start()
        }
    }

    fun pause() = mediaPlayer?.takeIf { it.isPlaying }?.pause()
    fun resume() = mediaPlayer?.takeIf { !it.isPlaying }?.start()
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}