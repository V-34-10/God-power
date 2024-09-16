package com.divinee.puwer.view.games.findcards

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.models.FindCard
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogVictoryGame

class FindPairClickHandler(
    private val adapterManager: FindPairAdapterManager,
    private val pairItemManager: PairItemManager,
    private val timerAnimation: TimerAnimation,
    private val bindingSetup: BindingSetup
) {
    private val delayHandler = 1000L
    private var firstPair: FindCard? = null
    private var secondPair: FindCard? = null
    private var flippingPair = false
    var stepSearchPair = 0
    var isGameStarted = false

    fun setupClickListener(
        context: Context,
        binding: FragmentFindCardsGameBinding,
        gameManager: FindPairGameManager
    ) {
        adapterManager.setOnItemClickListener { pairItem, position ->
            onPairClick(pairItem, position, context, binding, gameManager)
        }
    }

    private fun onPairClick(
        pairItem: FindCard,
        position: Int,
        context: Context,
        binding: FragmentFindCardsGameBinding,
        gameManager: FindPairGameManager
    ) {
        if (isInvalidClick(pairItem)) return

        startGameIfNotStarted(binding, context, gameManager)

        pairItem.flipItem = true
        pairItem.positionItem = position
        adapterManager.notifyItemChanged(position)

        if (firstPair == null) {
            firstPair = pairItem
        } else {
            secondPair = pairItem
            flippingPair = true
            Handler(Looper.getMainLooper()).postDelayed({
                checkMatchPair(context, gameManager)
            }, delayHandler)
        }
    }

    private fun isInvalidClick(pairItem: FindCard): Boolean =
        flippingPair || pairItem.flipItem || pairItem.matchItem

    private fun startGameIfNotStarted(
        binding: FragmentFindCardsGameBinding,
        context: Context,
        gameManager: FindPairGameManager
    ) {
        if (!isGameStarted) {
            timerAnimation.startTimer(binding, context, gameManager)
            isGameStarted = true
        }
    }

    private fun checkMatchPair(context: Context, gameManager: FindPairGameManager) {
        if (firstPair?.picture == secondPair?.picture) {
            markPairsAsMatched()
        } else {
            resetPairSelection()
        }

        stepSearchPair++
        adapterManager.notifyItemChanged(firstPair?.positionItem ?: -1)
        adapterManager.notifyItemChanged(secondPair?.positionItem ?: -1)

        resetSelection()

        if (checkGameOver()) {
            runDialogVictoryGame(context, gameManager, bindingSetup)
        }
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

    private fun checkGameOver(): Boolean = pairItemManager.getPairList().all { it.matchItem }
}