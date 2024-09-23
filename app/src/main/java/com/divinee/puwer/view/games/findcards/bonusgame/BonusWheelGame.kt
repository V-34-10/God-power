package com.divinee.puwer.view.games.findcards.bonusgame

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.models.SectorWheel
import kotlin.math.roundToInt

object BonusWheelGame {
    private var startAngleRotateWheel = 0f
    private const val DURATION = 2000L
    private const val BET = 200

    fun animatedRotateWheel(
        binding: FragmentFindCardsGameBinding,
        onAnimationEnd: () -> Unit = {},
        context: Context
    ) {
        val targetAngle = startAngleRotateWheel + 360f + randomAngle()
        val rotationAnimation = RotateAnimation(
            startAngleRotateWheel,
            targetAngle,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = DURATION
            fillAfter = true
            setAnimationListener(null)
        }
        rotationAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startAngleRotateWheel = (targetAngle) % 360f

                updateStatusBalance(
                    winCoeffSector(startAngleRotateWheel).coefficient,
                    binding,
                    context
                )

                onAnimationEnd()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        binding.wheel.startAnimation(rotationAnimation)
    }

    private fun angleToDegree(angle: Float): Float =
        (angle % 360).let { if (it < 0) it + 360 else it }

    private fun randomAngle(): Float = listOf(20f, 70f, 110f, 160f, 200f, 240f, 290f, 340f).random()

    private fun winCoeffSector(angle: Float): SectorWheel {
        return when (angleToDegree(angle)) {
            in 0f..40f -> SectorWheel(2.0f, 20f)
            in 41f..89f -> SectorWheel(1.0f, 70f)
            in 90f..134f -> SectorWheel(2.0f, 110f)
            in 135f..180f -> SectorWheel(3.0f, 160f)
            in 181f..220f -> SectorWheel(0.0f, 200f)
            in 221f..269f -> SectorWheel(5.0f, 240f)
            in 270f..314f -> SectorWheel(4.0f, 290f)
            in 315f..360f -> SectorWheel(0.0f, 340f)
            else -> SectorWheel(0.0f, 0f)
        }
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun updateStatusBalance(
        coefficient: Float,
        binding: FragmentFindCardsGameBinding,
        context: Context
    ) {
        var balance = stringToNumber(binding.textBalance.text.toString())
        if (coefficient != 0f) {
            val scoreWin = (BET * coefficient).roundToInt()
            balance += scoreWin
        }
        binding.textBalance.text = balance.toString()
        context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit().putString(
            "balanceScores",
            balance.toString()
        ).apply()
    }

    fun stringToNumber(text: String): Int =
        text.filter { it.isDigit() }.toIntOrNull() ?: 0
}