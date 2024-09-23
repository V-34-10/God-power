package com.divinee.puwer.decoration

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat

object Edge {

    @RequiresApi(Build.VERSION_CODES.R)
    fun enableEdgeToEdge(activity: Activity) {
        val activityWindow = activity.window
        WindowCompat.setDecorFitsSystemWindows(activityWindow, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activityWindow.insetsController?.apply {
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            activityWindow.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}