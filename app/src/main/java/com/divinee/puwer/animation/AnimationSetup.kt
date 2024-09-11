package com.divinee.puwer.animation

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.divinee.puwer.R

object AnimationSetup {
    private var scaleAnimation: Animation? = null

    fun startAnimation(context: Context): Animation {
        if (scaleAnimation == null) {
            scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
        }
        return scaleAnimation!!
    }

    fun clearAnimationCache() {
        scaleAnimation = null
    }
}