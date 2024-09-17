package com.divinee.puwer.animation

import android.animation.ValueAnimator
import android.content.Context
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.viewbinding.ViewBinding
import com.divinee.puwer.R
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogLoseGame
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogLoseGamePuzzle
import com.divinee.puwer.view.games.findcards.FindPairGameManager

class TimerAnimation {
    private val delayTimer = 1000L
    private var timer: CountDownTimer? = null
    private var startTime: Long = 0
    var elapsedTime: Long = 0
    private val animationDuration = 60000L
    private var initialWidth = 0

    fun startTimer(
        binding: ViewBinding,
        context: Context,
        gameManager: FindPairGameManager?
    ) {
        if (initialWidth == 0) {
            when (binding) {
                is FragmentFindCardsGameBinding -> initialWidth =
                    binding.timerCounter.lineTimer.width

                is FragmentPuzzleGameBinding -> initialWidth = binding.timerCounter.lineTimer.width
            }
        }
        startTime = System.currentTimeMillis() - elapsedTime
        timer = object : CountDownTimer(animationDuration, delayTimer) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = System.currentTimeMillis() - startTime
                when (binding) {
                    is FragmentFindCardsGameBinding -> updateLineTimerWidth(
                        millisUntilFinished,
                        binding
                    )

                    is FragmentPuzzleGameBinding -> updateLineTimerWidth(
                        millisUntilFinished,
                        binding
                    )
                }
            }

            override fun onFinish() {
                when (binding) {
                    is FragmentFindCardsGameBinding -> runDialogLoseGame(context, gameManager)
                    is FragmentPuzzleGameBinding ->
                        runDialogLoseGamePuzzle(
                            context,
                            this@TimerAnimation,
                            binding,
                            context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                                .getString("levelGame", context.getString(R.string.easy_level_btn))
                                .toString()
                        )
                    }
                }
            }.start()
        }

    private fun updateLineTimerWidth(
        millisUntilFinished: Long,
        binding: ViewBinding
    ) {
        val lineTimer: View? = when (binding) {
            is FragmentFindCardsGameBinding -> binding.timerCounter.lineTimer
            is FragmentPuzzleGameBinding -> binding.timerCounter.lineTimer
            else -> null
        }

        val newWidth = (millisUntilFinished / animationDuration.toFloat() * initialWidth).toInt()

        val animation = lineTimer?.width?.let { ValueAnimator.ofInt(it, newWidth) }
        animation?.duration = delayTimer
        animation?.addUpdateListener {
            val value = it.animatedValue as Int
            val layoutParams = lineTimer.layoutParams
            layoutParams?.width = value
            lineTimer.layoutParams = layoutParams
        }
        animation?.start()
    }

    fun stopTimer(
        binding: ViewBinding
    ) {
        timer?.cancel()
        when (binding) {
            is FragmentFindCardsGameBinding -> resetTimer(binding)
            is FragmentPuzzleGameBinding -> resetTimer(binding)
        }
    }

    private fun resetTimer(binding: ViewBinding) {
        startTime = 0
        elapsedTime = 0
        when (binding) {
            is FragmentFindCardsGameBinding -> resetLineTimerWidth(binding)
            is FragmentPuzzleGameBinding -> resetLineTimerWidth(binding)
        }
    }

    private fun resetLineTimerWidth(binding: ViewBinding) {
        val lineTimer: View? = when (binding) {
            is FragmentFindCardsGameBinding -> binding.timerCounter.lineTimer
            is FragmentPuzzleGameBinding -> binding.timerCounter.lineTimer
            else -> null
        }
        val layoutParams = lineTimer?.layoutParams
        layoutParams?.width = initialWidth
        lineTimer?.layoutParams = layoutParams
    }
}