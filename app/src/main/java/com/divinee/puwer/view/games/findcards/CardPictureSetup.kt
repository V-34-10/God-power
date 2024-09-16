package com.divinee.puwer.view.games.findcards

import com.divinee.puwer.R

data class ColumnConfig(
    val images: List<Int>,
    val rows: Int,
    val columns: Int,
    val repeatTimes: Int = 2,
    val extraElements: Int = 0
)

object CardPictureSetup {
    private val levelConfiguration = ColumnConfig(
        images = listOf(
            R.drawable.find_1, R.drawable.find_2, R.drawable.find_3, R.drawable.find_4,
            R.drawable.find_5, R.drawable.find_6
        ),
        rows = 4,
        columns = 3
    )

    fun getImages(): List<Int> {
        val neededSize = levelConfiguration.rows * levelConfiguration.columns
        return levelConfiguration.images.repeatElements(levelConfiguration.repeatTimes)
            .take(neededSize)
    }

    private fun List<Int>.repeatElements(repeatTimes: Int): List<Int> {
        val result = mutableListOf<Int>()
        repeat(repeatTimes) {
            result.addAll(this)
        }
        return result
    }
}