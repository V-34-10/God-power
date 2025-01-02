package com.divinee.puwer.view.games.puzzle

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.divinee.puwer.adapters.PuzzleAdapter
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding

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
            puzzleImageSetup.preparationPuzzles(selectLevel),
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