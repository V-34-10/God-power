package com.divinee.puwer.view.games.puzzle

import android.content.Context
import com.divinee.puwer.models.LevelPuzzleConfig

class PuzzleImageSetup(private val context: Context) {

    private fun generatePuzzleList(
        levelName: String,
        count: Int,
        additionalLast: Int? = null
    ): List<Int> {
        val baseName = "puzzle_$levelName"
        val baseNameLast = "puzzle_easy_"
        val list = (0 until count).map { resIdForName("$baseName$it") }.toMutableList()
        additionalLast?.let { list.add(resIdForName("$baseNameLast$it")) }
        return list
    }

    private fun resIdForName(name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    private val levelConfig = mapOf(
        "Easy" to LevelPuzzleConfig(
            winListPuzzle = generatePuzzleList("easy_", 9),
            spanPuzzle = 3,
            endPuzzle = 8
        ),
        "Medium" to LevelPuzzleConfig(
            winListPuzzle = generatePuzzleList("medium_", 15, additionalLast = 8),
            spanPuzzle = 4,
            endPuzzle = 15
        ),
        "Hard" to LevelPuzzleConfig(
            winListPuzzle = generatePuzzleList("hard_", 24, additionalLast = 8),
            spanPuzzle = 5,
            endPuzzle = 24
        )
    )

    fun getLevelConfig(level: String): LevelPuzzleConfig =
        levelConfig[level] ?: levelConfig["Easy"]!!
}