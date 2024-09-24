package com.divinee.puwer.view.games.puzzle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.divinee.puwer.R
import com.divinee.puwer.adapters.MovePuzzleListener
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.view.games.BaseFragment
import com.divinee.puwer.view.levels.LevelActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class PuzzleGameFragment : BaseFragment<FragmentPuzzleGameBinding>(), MovePuzzleListener {
    private lateinit var gameController: GameController
    private lateinit var selectLevel: String
    private lateinit var timerAnimation: TimerAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPuzzleGameBinding.inflate(layoutInflater, container, false)
        gameController = GameController
        selectLevel = requireContext().getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
            .getString("levelGame", this.getString(R.string.easy_level_btn)).toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicSet = MusicSetup(requireContext())
        MusicRunner.musicStartMode(requireContext(), R.raw.music__puzzle, musicSet)
        updatePreviewImagePuzzleForLevel()
        binding.textBalance.text =
            context?.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)?.getString(
                "balanceScores",
                context?.getString(R.string.text_default_balance)
            )
        timerAnimation = TimerAnimation()
        gameController.startGame(timerAnimation, binding, selectLevel, requireContext())
        observeControlBarGame()
    }

    private fun updatePreviewImagePuzzleForLevel() {
        when (selectLevel) {
            this.getString(R.string.medium_level_btn) -> binding.previewPuzzle.setImageResource(R.drawable.preview_medium_puzzle)
            this.getString(R.string.hard_level_btn) -> binding.previewPuzzle.setImageResource(R.drawable.preview_hard_puzzle)
            else -> binding.previewPuzzle.setImageResource(R.drawable.preview_easy_puzzle)
        }
    }

    override fun observeControlBarGame() {
        super.observeControlBarGame()
        val btnChange = requireView().findViewById<View>(R.id.text_change)
        btnChange.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, LevelActivity::class.java))
            activity?.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerAnimation.stopTimer(binding)
    }

    override fun onMovePuzzle(move: Int) {
        gameController.moveCount = move
    }
}