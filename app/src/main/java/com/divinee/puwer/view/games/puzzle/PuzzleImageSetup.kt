package com.divinee.puwer.view.games.puzzle

import com.divinee.puwer.R
import com.divinee.puwer.models.LevelPuzzleConfig

object PuzzleImageSetup {
    private val levelConfig = mapOf(
        "Easy" to LevelPuzzleConfig(
            winListPuzzle = listOf(
                R.drawable.puzzle_easy_0, R.drawable.puzzle_easy_1, R.drawable.puzzle_easy_2,
                R.drawable.puzzle_easy_3, R.drawable.puzzle_easy_4, R.drawable.puzzle_easy_5,
                R.drawable.puzzle_easy_6, R.drawable.puzzle_easy_7, R.drawable.puzzle_easy_8
            ),
            spanPuzzle = 3,
            endPuzzle = 8
        ),
        "Medium" to LevelPuzzleConfig(
            winListPuzzle = listOf(
                R.drawable.puzzle_medium_0, R.drawable.puzzle_medium_1, R.drawable.puzzle_medium_2, R.drawable.puzzle_medium_3,
                R.drawable.puzzle_medium_4, R.drawable.puzzle_medium_5, R.drawable.puzzle_medium_6, R.drawable.puzzle_medium_7,
                R.drawable.puzzle_medium_8, R.drawable.puzzle_medium_9, R.drawable.puzzle_medium_10, R.drawable.puzzle_medium_11,
                R.drawable.puzzle_medium_12, R.drawable.puzzle_medium_13, R.drawable.puzzle_medium_14, R.drawable.puzzle_easy_8
            ),
            spanPuzzle = 4,
            endPuzzle = 15
        ),
        "Hard" to LevelPuzzleConfig(
            winListPuzzle = listOf(
                R.drawable.puzzle_hard_0, R.drawable.puzzle_hard_1, R.drawable.puzzle_hard_2, R.drawable.puzzle_hard_3, R.drawable.puzzle_hard_4,
                R.drawable.puzzle_hard_5, R.drawable.puzzle_hard_6, R.drawable.puzzle_hard_7, R.drawable.puzzle_hard_8, R.drawable.puzzle_hard_9,
                R.drawable.puzzle_hard_10, R.drawable.puzzle_hard_11, R.drawable.puzzle_hard_12, R.drawable.puzzle_hard_13, R.drawable.puzzle_hard_14,
                R.drawable.puzzle_hard_15, R.drawable.puzzle_hard_16, R.drawable.puzzle_hard_17, R.drawable.puzzle_hard_18, R.drawable.puzzle_hard_19,
                R.drawable.puzzle_hard_20, R.drawable.puzzle_hard_21, R.drawable.puzzle_hard_22, R.drawable.puzzle_hard_23, R.drawable.puzzle_easy_8
            ),
            spanPuzzle = 5,
            endPuzzle = 24
        )
    )

    fun getLevelConfig(level: String): LevelPuzzleConfig = levelConfig[level] ?: levelConfig["Easy"]!!
}