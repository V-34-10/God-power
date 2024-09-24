package com.divinee.puwer.view.games.puzzle

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.divinee.puwer.R
import com.divinee.puwer.adapters.PuzzleAdapter
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.models.Puzzle


@SuppressLint("StaticFieldLeak")
object GameController {
    private lateinit var puzzleAdapter: PuzzleAdapter
    var moveCount = 0

    fun startGame(
        timerAnimation: TimerAnimation,
        binding: FragmentPuzzleGameBinding,
        selectLevel: String,
        context: Context
    ) {
        initRecyclerAdapter(binding, selectLevel, context, timerAnimation)
    }

    fun restartGame(
        timerAnimation: TimerAnimation,
        binding: FragmentPuzzleGameBinding,
        selectLevel: String,
        context: Context
    ) {
        moveCount = 0
        timerAnimation.stopTimer(binding)
        initRecyclerAdapter(binding, selectLevel, context, timerAnimation)
    }

    @SuppressLint("DiscouragedApi")
    private fun getShuffledPuzzles(selectLevel: String, context: Context): MutableList<Puzzle> {
        val puzzleImageSetup = PuzzleImageSetup(context)
        val levelConfig = puzzleImageSetup.getLevelConfig(selectLevel)
        val resourcesPuzzles = levelConfig.winListPuzzle.dropLast(1)
        val sectorPuzzle =
            resourcesPuzzles.mapIndexed { index, resourceId -> Puzzle(resourceId, index) }
                .toMutableList()

        sectorPuzzle.shuffle()
        sectorPuzzle.add(
            Puzzle(
                R.drawable.puzzle_easy_8,
                levelConfig.endPuzzle
            )
        )
        return sectorPuzzle
    }

    private fun initRecyclerAdapter(
        binding: FragmentPuzzleGameBinding,
        selectLevel: String,
        context: Context,
        timerAnimation: TimerAnimation
    ) {
        val puzzleImageSetup = PuzzleImageSetup(context)
        val levelConfig = puzzleImageSetup.getLevelConfig(selectLevel)
        puzzleAdapter = PuzzleAdapter(
            binding.sceneCard,
            getShuffledPuzzles(selectLevel, context),
            selectLevel,
            context,
            timerAnimation,
            binding
        )
        binding.sceneCard.layoutManager =
            GridLayoutManager(context, levelConfig.spanPuzzle)
        binding.sceneCard.adapter = puzzleAdapter
    }
}