package com.divinee.puwer.view.games.findcards

import com.divinee.puwer.models.FindCard

class PairItemManager {
    private val pairList = mutableListOf<FindCard>()
    private val imageList = mutableListOf<Int>()

    fun setupPairItems() {
        pairList.clear()
        imageList.apply {
            clear()
            addAll(CardPictureSetup.getImages())
        }

        var position = 0
        repeat(getNumPairs()) {
            val imageRes = imageList[it]
            pairList.add(FindCard(imageRes, positionItem = position++))
            pairList.add(FindCard(imageRes, positionItem = position++))
        }

        pairList.shuffle()
    }

    fun getPairList(): List<FindCard> = pairList

    fun resetPairs() {
        pairList.forEach { it.reset() }
    }

    private fun FindCard.reset() {
        flipItem = false
        matchItem = false
    }

}

fun getNumPairs(): Int = 6