package com.divinee.puwer.view.settings

import android.content.Context
import androidx.annotation.RawRes
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class MusicSetup(private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    private var currentSoundResId: Int? = null

    fun playSound(@RawRes soundResId: Int, loop: Boolean = false) {
        if (currentSoundResId == soundResId && exoPlayer?.isPlaying == true) return
        release()
        val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/$soundResId")
        exoPlayer = ExoPlayer.Builder(context)
            .build()
            .also {
                it.setMediaItem(mediaItem)
                it.repeatMode = if (loop) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                it.prepare()
                it.playWhenReady = true
                currentSoundResId = soundResId
            }
    }

    fun pause() = exoPlayer?.pause()
    fun resume() = exoPlayer?.play()

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        currentSoundResId = null
    }
}