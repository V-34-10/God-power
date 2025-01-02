package com.divinee.puwer.view.games.puzzle

import android.annotation.SuppressLint
import android.content.Context
import com.divinee.puwer.R
import com.divinee.puwer.models.LevelPuzzleConfig
import com.divinee.puwer.models.Puzzle
import java.util.Collections

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

    @SuppressLint("DiscouragedApi")
    fun preparationPuzzles(level: String): MutableList<Puzzle> {
        val levelConfig = getLevelConfig(level)
        val initialPuzzles = createInitialPuzzles(levelConfig)
        return generateShuffledPuzzle(initialPuzzles, levelConfig.spanPuzzle)
    }

    private fun createInitialPuzzles(levelConfig: LevelPuzzleConfig): MutableList<Puzzle> =
        levelConfig.winListPuzzle.mapIndexed { index, resourceId -> Puzzle(resourceId, index) }
            .toMutableList()

    private fun generateShuffledPuzzle(
        initialPuzzles: MutableList<Puzzle>,
        gridSize: Int
    ): MutableList<Puzzle> {
        val puzzles = initialPuzzles.toMutableList()
        var emptyTileIndex = puzzles.size - 1

        val shuffleMoves = 1000
        repeat(shuffleMoves) {
            val possibleMoves = getPossibleMoves(emptyTileIndex, gridSize)

            if (possibleMoves.isEmpty()) {
                puzzles.shuffle()
                emptyTileIndex = puzzles.indexOfFirst { it.image == R.drawable.puzzle_easy_8 }
            } else {
                val moveToIndex = possibleMoves.random()
                Collections.swap(puzzles, emptyTileIndex, moveToIndex)
                emptyTileIndex = moveToIndex
            }
        }

        val emptyTile = puzzles.removeAt(emptyTileIndex)
        puzzles.add(emptyTile)
        return puzzles
    }

    private fun getPossibleMoves(emptyTileIndex: Int, gridSize: Int): List<Int> {
        val possibleMoves = mutableListOf<Int>()

        val row = emptyTileIndex / gridSize
        val col = emptyTileIndex % gridSize

        if (row > 0) possibleMoves.add(emptyTileIndex - gridSize)
        if (row < gridSize - 1) possibleMoves.add(emptyTileIndex + gridSize)
        if (col > 0) possibleMoves.add(emptyTileIndex - 1)
        if (col < gridSize - 1) possibleMoves.add(emptyTileIndex + 1)

        return possibleMoves
    }
}