package com.divinee.puwer.view.games.puzzle

import android.view.View
import com.divinee.puwer.models.Puzzle
import java.util.Collections
import kotlin.math.abs

object PuzzleHelper {

    fun canMoveStepGame(clickedPosition: Int, emptyPosition: Int, gridSize: Int): Boolean {
        val rowDiff = abs((clickedPosition / gridSize) - (emptyPosition / gridSize))
        val colDiff = abs((clickedPosition % gridSize) - (emptyPosition % gridSize))
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }

    fun checkCard(cardList: List<Puzzle>, correctCard: List<Int>): Boolean = cardList.zip(correctCard).all { (card, correct) -> card.image == correct }

    fun swapCardsGame(cardList: MutableList<Puzzle>, fromPosition: Int, toPosition: Int) {
        Collections.swap(cardList, fromPosition, toPosition)
        cardList[fromPosition].position = fromPosition
        cardList[toPosition].position = toPosition
    }

    fun animatedPuzzle(view: View) {
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction {
            view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
        }.start()
    }
}