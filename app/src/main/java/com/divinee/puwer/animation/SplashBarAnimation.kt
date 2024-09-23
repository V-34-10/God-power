package com.divinee.puwer.animation

import android.animation.ValueAnimator
import android.content.Context
import android.view.View

object SplashBarAnimation {

    fun View.loadingAnim(duration: Long = 20000L, maxWidth: Int) {
        val layoutParams = this.layoutParams
        ValueAnimator.ofInt(1, maxWidth).apply {
            this.duration = duration
            addUpdateListener {
                layoutParams.width = it.animatedValue as Int
                this@loadingAnim.layoutParams = layoutParams
            }
            start()
        }
    }

    fun Context.returnProgressWidth(): Int = (250 * resources.displayMetrics.density).toInt() - 1
}