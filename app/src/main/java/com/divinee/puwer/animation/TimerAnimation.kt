package com.divinee.puwer.animation

import android.animation.ValueAnimator
import android.content.Context
import android.os.CountDownTimer
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogLoseGame
import com.divinee.puwer.view.games.findcards.FindPairGameManager

class TimerAnimation {
    private val delayTimer = 1000L
    private var timer: CountDownTimer? = null
    private var startTime: Long = 0
    var elapsedTime: Long = 0
    private val animationDuration = 60000L
    private var initialWidth = 0

    fun startTimer(
        binding: FragmentFindCardsGameBinding,
        context: Context,
        gameManager: FindPairGameManager
    ) {
        if (initialWidth == 0) {
            initialWidth = binding.timerCounter.lineTimer.width
        }
        startTime = System.currentTimeMillis() - elapsedTime
        timer = object : CountDownTimer(animationDuration, delayTimer) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = System.currentTimeMillis() - startTime
                updateLineTimerWidth(millisUntilFinished, binding)
            }

            override fun onFinish() {
                runDialogLoseGame(context, gameManager)
            }
        }.start()
    }

    private fun updateLineTimerWidth(
        millisUntilFinished: Long,
        binding: FragmentFindCardsGameBinding
    ) {
        val lineTimer = binding.timerCounter.lineTimer
        val newWidth = (millisUntilFinished / animationDuration.toFloat() * initialWidth).toInt()

        val animation = ValueAnimator.ofInt(lineTimer.width, newWidth)
        animation.duration = delayTimer
        animation.addUpdateListener {
            val value = it.animatedValue as Int
            val layoutParams = lineTimer.layoutParams
            layoutParams.width = value
            lineTimer.layoutParams = layoutParams
        }
        animation.start()
    }

    fun stopTimer(
        binding: FragmentFindCardsGameBinding
    ) {
        timer?.cancel()
        resetTimer(binding)
    }

    private fun resetTimer(binding: FragmentFindCardsGameBinding) {
        startTime = 0
        elapsedTime = 0
        resetLineTimerWidth(binding)
    }

    private fun resetLineTimerWidth(binding: FragmentFindCardsGameBinding) {
        val lineTimer = binding.timerCounter.lineTimer
        val layoutParams = lineTimer.layoutParams
        layoutParams.width = initialWidth
        lineTimer.layoutParams = layoutParams
    }
}