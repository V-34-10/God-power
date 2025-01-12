package com.divinee.puwer.view.games.findcards

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.models.FindCard
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogVictoryGame

class CardClickHandler(
    private val adapterManager: AdapterManager,
    private val cardItemManager: CardItemManager,
    private val timerAnimation: TimerAnimation,
    private val bindingSetup: BindingSetup
) {
    private var firstPair: FindCard? = null
    private var secondPair: FindCard? = null
    private var flippingPair = false
    var stepSearchPair = 0
    var isGameStarted = false

    fun setupClickListener(
        context: Context,
        binding: FragmentFindCardsGameBinding,
        gameManager: CardGameManager
    ) {
        adapterManager.setOnItemClickListener { pairItem, position ->
            handleClickIfValid(pairItem, position, context, binding, gameManager)
        }
    }

    private fun handleClickIfValid(
        pairItem: FindCard,
        position: Int,
        context: Context,
        binding: FragmentFindCardsGameBinding,
        gameManager: CardGameManager
    ) {
        if (!isValidClick(pairItem)) return

        if (!isGameStarted) startGame(binding, context, gameManager)

        flipCard(pairItem, position)
        processCardSelection(pairItem, context, gameManager, binding)
    }

    private fun flipCard(pairItem: FindCard, position: Int) {
        pairItem.apply {
            flipItem = true
            positionItem = position
        }
        adapterManager.notifyItemChanged(position)
    }

    private fun processCardSelection(
        pairItem: FindCard,
        context: Context,
        gameManager: CardGameManager,
        binding: FragmentFindCardsGameBinding,
    ) {
        if (firstPair == null) {
            firstPair = pairItem
        } else {
            secondPair = pairItem
            flippingPair = true
            delayCheckForMatch(context, gameManager, binding)
        }
    }

    private fun delayCheckForMatch(context: Context, gameManager: CardGameManager, binding: FragmentFindCardsGameBinding,) {
        Handler(Looper.getMainLooper()).postDelayed({
            checkMatchAndProceed(context, gameManager, binding)
        }, 1000L)
    }

    private fun checkMatchAndProceed(context: Context, gameManager: CardGameManager, binding: FragmentFindCardsGameBinding,) {
        if (areCardsMatched()) {
            markPairsAsMatched()
        } else {
            resetPairSelection()
        }
        updateGameState(context, gameManager, binding)
    }

    private fun areCardsMatched(): Boolean {
        return firstPair?.picture == secondPair?.picture
    }

    private fun isValidClick(pairItem: FindCard): Boolean {
        return !flippingPair && !pairItem.flipItem && !pairItem.matchItem
    }

    private fun startGame(
        binding: FragmentFindCardsGameBinding,
        context: Context,
        gameManager: CardGameManager
    ) {
        timerAnimation.startTimer(binding, context, gameManager)
        isGameStarted = true
    }

    private fun updateGameState(context: Context, gameManager: CardGameManager, binding: FragmentFindCardsGameBinding,) {
        incrementStepCounter()
        refreshDisplayedCards()
        resetSelection()

        if (checkGameOver()) {
            runDialogVictoryGame(context, gameManager, bindingSetup)
            timerAnimation.stopTimer(binding)
        }
    }

    private fun incrementStepCounter() {
        stepSearchPair++
    }

    private fun refreshDisplayedCards() {
        firstPair?.let { adapterManager.notifyItemChanged(it.positionItem) }
        secondPair?.let { adapterManager.notifyItemChanged(it.positionItem) }
    }

    private fun markPairsAsMatched() {
        firstPair?.matchItem = true
        secondPair?.matchItem = true
    }

    private fun resetPairSelection() {
        firstPair?.flipItem = false
        secondPair?.flipItem = false
    }

    private fun resetSelection() {
        firstPair = null
        secondPair = null
        flippingPair = false
    }

    private fun checkGameOver(): Boolean = cardItemManager.getPairList().all { it.matchItem }
}