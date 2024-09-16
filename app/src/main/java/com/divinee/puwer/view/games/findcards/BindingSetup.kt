package com.divinee.puwer.view.games.findcards

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.view.games.findcards.bonusgame.BonusWheelGame.animatedRotateWheel

class BindingSetup(private val binding: FragmentFindCardsGameBinding) {

    fun setupRecyclerView(context: Context): RecyclerView {
        val recyclerView = binding.sceneFindGame
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        return recyclerView
    }

    private fun visibleBonusGame() {
        binding.statusBalance.visibility = View.GONE
        binding.btnInfo.visibility = View.GONE
        binding.timerCounter.lineTimer.visibility = View.GONE
        binding.timerCounter.timerBackground.visibility = View.GONE
        binding.sceneFindGame.visibility = View.GONE
        binding.btnNext.visibility = View.GONE

        binding.textTitleTryLuck.visibility = View.VISIBLE
        binding.blockWheel.visibility = View.VISIBLE
        binding.btnGo.visibility = View.VISIBLE
    }

    private fun goneBonusGame() {
        binding.statusBalance.visibility = View.VISIBLE
        binding.btnInfo.visibility = View.VISIBLE
        binding.timerCounter.lineTimer.visibility = View.VISIBLE
        binding.timerCounter.timerBackground.visibility = View.VISIBLE
        binding.sceneFindGame.visibility = View.VISIBLE
        binding.btnNext.visibility = View.VISIBLE

        binding.textTitleTryLuck.visibility = View.GONE
        binding.blockWheel.visibility = View.GONE
        binding.btnGo.visibility = View.GONE
    }

    fun observeButtonBonusGame(context: Context) {
        visibleBonusGame()
        binding.btnGo.setOnClickListener {
            it.isEnabled = false
            it.startAnimation(startAnimation(context))
            animatedRotateWheel(binding, {
                it.isEnabled = true
            }, context)

            Handler(Looper.getMainLooper()).postDelayed({
                goneBonusGame()
            }, 3000)
        }
    }
}