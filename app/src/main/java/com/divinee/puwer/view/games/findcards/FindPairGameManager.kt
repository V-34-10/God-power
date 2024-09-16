package com.divinee.puwer.view.games.findcards

import android.content.Context
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding

class FindPairGameManager(
    private val context: Context,
    private val binding: FragmentFindCardsGameBinding
) {
    private val timerAnimation = TimerAnimation()
    private val bindingSetup = BindingSetup(binding)
    private val adapterManager = FindPairAdapterManager(bindingSetup)
    private val pairItemManager = PairItemManager()
    private val clickHandler = FindPairClickHandler(adapterManager, pairItemManager, timerAnimation, bindingSetup)

    fun initGame() {
        adapterManager.initRecyclerView(context)
        pairItemManager.setupPairItems()
        adapterManager.setupAdapter(pairItemManager.getPairList())
        clickHandler.setupClickListener(context, binding, this)
    }

    fun resetGame() {
        pairItemManager.resetPairs()
        adapterManager.resetAdapter()
        clickHandler.stepSearchPair = 0
        timerAnimation.stopTimer(binding)
        clickHandler.isGameStarted = false
    }
}