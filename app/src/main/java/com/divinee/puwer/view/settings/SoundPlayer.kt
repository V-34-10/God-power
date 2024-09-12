package com.divinee.puwer.view.settings

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.annotation.RawRes

class SoundPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isLooping = false
    private val handler = Handler(Looper.getMainLooper())

    fun play(@RawRes soundResId: Int, loop: Boolean = false) {
        if (isPlaying()) return

        isLooping = loop
        mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = this@SoundPlayer.isLooping
            setOnCompletionListener {
                if (!isLooping) {
                    handler.post { release() }
                }
            }
            start()
        }
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
    }

    fun release() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    private fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
}