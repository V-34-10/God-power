package com.divinee.puwer.view

import androidx.appcompat.app.AppCompatActivity
import com.divinee.puwer.view.settings.MusicSetup

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var musicSet: MusicSetup

    override fun onResume() {
        super.onResume()
        musicSet.resume()
    }

    override fun onPause() {
        super.onPause()
        musicSet.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicSet.release()
    }
}