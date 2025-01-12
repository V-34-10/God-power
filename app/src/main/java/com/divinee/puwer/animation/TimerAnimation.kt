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
import com.divinee.puwer.view.games.findcards.CardGameManager
import com.divinee.puwer.view.games.findcards.bonusgame.BonusWheelGame.stringToNumber

class TimerAnimation {
    private val delayTimer = 1000L
    private var timer: CountDownTimer? = null
    private var startTime: Long = 0
    var elapsedTime: Long = 0
    private val animationDuration = 60000L
    private var initialWidth = 0
    private var valueAnimator: ValueAnimator? = null

    fun startTimer(
        binding: ViewBinding,
        context: Context,
        gameManager: CardGameManager?
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
                    is FragmentFindCardsGameBinding -> {
                        runDialogLoseGame(context, gameManager)
                        balanceWhenLoseGame(binding, context)
                    }

                    is FragmentPuzzleGameBinding -> {
                        runDialogLoseGamePuzzle(
                            context,
                            this@TimerAnimation,
                            binding,
                            context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                                .getString("levelGame", context.getString(R.string.easy_level_btn))
                                .toString()
                        )
                        balanceWhenLoseGame(binding, context)
                    }
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

        valueAnimator?.cancel()
        valueAnimator = lineTimer?.width?.let { ValueAnimator.ofInt(it, newWidth) }?.apply {
            duration = delayTimer
            addUpdateListener {
                val value = it.animatedValue as Int
                val layoutParams = lineTimer.layoutParams
                layoutParams?.width = value
                lineTimer.layoutParams = layoutParams
            }
            start()
        }
    }

    fun stopTimer(
        binding: ViewBinding
    ) {
        timer?.cancel()
        valueAnimator?.cancel()
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

    private fun balanceWhenLoseGame(binding: ViewBinding, context: Context) {
        val balanceTextView = when (binding) {
            is FragmentFindCardsGameBinding -> binding.textBalance
            is FragmentPuzzleGameBinding -> binding.textBalance
            else -> return
        }

        var balance = stringToNumber(balanceTextView.text.toString()) ?: 0

        if (balance > 0) {
            balance -= 200
            balance = maxOf(0, balance)
        }
        balanceTextView.text = balance.toString()
        context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
            .putString("balanceScores", balance.toString()).apply()
    }
}