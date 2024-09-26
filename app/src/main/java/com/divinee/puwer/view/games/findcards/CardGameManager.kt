package com.divinee.puwer.view.games.findcards

import android.content.Context
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding

class CardGameManager(
    private val context: Context,
    private val binding: FragmentFindCardsGameBinding
) {
    private val gameComponents: GameComponents = createGameComponents(binding)

    init {
        gameComponents.pairItemManager.setupPairItems()
        gameComponents.adapterManager.apply {
            initRecyclerView(context)
            setupAdapter(gameComponents.pairItemManager.getPairList())
        }
        gameComponents.clickHandler.setupClickListener(
            context,
            binding,
            this
        )
    }

    fun stopGame() {
        resetClickHandlerState()
        gameComponents.timerAnimation.stopTimer(binding)
        gameComponents.pairItemManager.resetPairs()
        gameComponents.pairItemManager.setupPairItems()
        gameComponents.adapterManager.resetAdapter()
    }


    private fun resetClickHandlerState() = gameComponents.clickHandler.apply {
        stepSearchPair = 0
        isGameStarted = false
    }
}