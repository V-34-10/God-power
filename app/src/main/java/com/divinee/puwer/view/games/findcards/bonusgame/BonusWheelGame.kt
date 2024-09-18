package com.divinee.puwer.view.games.findcards.bonusgame

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.models.SectorWheel
import java.util.Random
import kotlin.math.roundToInt

object BonusWheelGame {

    private var startAngleRotateWheel = 0f

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
            duration = 2000
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

    private fun angleToDegree(angleRotate: Float): Float {
        val degree = angleRotate % 360
        return if (degree < 0) degree + 360 else degree
    }

    private fun randomAngle(): Float {
        return when (Random().nextInt(8)) {
            0 -> 20f
            1 -> 70f
            2 -> 110f
            3 -> 160f
            4 -> 200f
            5 -> 240f
            6 -> 290f
            7 -> 340f
            else -> 0f
        }
    }

    private fun winCoeffSector(
        angleRotate: Float
    ): SectorWheel {
        return when (angleToDegree(angleRotate)) {
            in 0f..40f -> SectorWheel(2.0f, 20f) // 2x,
            in 41f..89f -> SectorWheel(1.0f, 70f) // 1x
            in 90f..134f -> SectorWheel(2.0f, 110f) // 2x
            in 135f..180f -> SectorWheel(3.0f, 160f) // 3x
            in 181f..220f -> SectorWheel(0.0f, 200f) // 0x
            in 221f..269f -> SectorWheel(5.0f, 240f) // 5x
            in 270f..314f -> SectorWheel(4.0f, 290f) // 4x
            in 315f..360f -> SectorWheel(0.0f, 340f) // 3x
            else -> {
                SectorWheel(0.0f, 0f)
            }
        }
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun updateStatusBalance(
        angle: Float,
        binding: FragmentFindCardsGameBinding,
        context: Context
    ) {
        var balance = stringToNumber(binding.textBalance.text.toString())
        val bet = 200
        if (angle.toInt() == 0) {
            balance -= 0
            binding.textBalance.text = "$balance"
        } else {
            val scoreWin = (bet.toFloat() * angle).roundToInt()
            balance += scoreWin
            binding.textBalance.text = "$balance"
        }
        context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit().putString(
            "balanceScores",
            balance.toString()
        ).apply()
    }

    fun stringToNumber(text: String): Int =
        text.replace(Regex("\\D"), "").toIntOrNull() ?: 0
}