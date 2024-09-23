package com.divinee.puwer.animation

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.LinearInterpolator

object SplashBarAnimation {

    fun View.loadingAnim(duration: Long = 15000L, maxWidth: Int) {
        layoutParams.width = 1
        ValueAnimator.ofInt(1, maxWidth).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            addUpdateListener {
                layoutParams.width = it.animatedValue as Int
                this@loadingAnim.layoutParams = layoutParams
            }
            start()
        }
    }

    fun Context.returnProgressWidth(): Int = (250 * resources.displayMetrics.density).toInt() - 1
}