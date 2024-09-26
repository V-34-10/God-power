package com.divinee.puwer.view.games.findcards

import android.content.Context
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding

class CardGameManager(
    private val context: Context,
    private val binding: FragmentFindCardsGameBinding
) {
    /*private val timerAnimation = TimerAnimation()
    private val bindingSetup = BindingSetup(binding)
    private val adapterManager = AdapterManager(bindingSetup)
    private val pairItemManager = CardItemManager()
    private val clickHandler =
        CardClickHandler(adapterManager, pairItemManager, timerAnimation, bindingSetup)*/

    private val gameComponents: GameComponents = createGameComponents(binding)

    /*fun initGame(lifecycleOwner: LifecycleOwner) {
        pairItemManager.setupPairItems()
        with(adapterManager) {
            initRecyclerView(context)
            setupAdapter(pairItemManager.getPairList())
        }
        clickHandler.setupClickListener(context, binding, this, lifecycleOwner)
    }

    fun resetGame() {
        pairItemManager.resetPairs()
        pairItemManager.setupPairItems()
        adapterManager.resetAdapter()
        resetClickHandlerState()
        timerAnimation.stopTimer(binding)
    }

    fun destroyGame() {
        resetClickHandlerState()
        timerAnimation.stopTimer(binding)
    }*/

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