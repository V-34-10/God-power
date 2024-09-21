package com.divinee.puwer.view.games.puzzle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.divinee.puwer.R
import com.divinee.puwer.adapters.MovePuzzleListener
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.view.levels.LevelActivity
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class PuzzleGameFragment : Fragment(), MovePuzzleListener {
    private lateinit var binding: FragmentPuzzleGameBinding
    private lateinit var musicSet: MusicSetup
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

    private fun observeControlBarGame() {
        val btnHome = requireView().findViewById<View>(R.id.btn_home)
        val btnInfo = requireView().findViewById<View>(R.id.btn_info)
        val btnChange = requireView().findViewById<View>(R.id.text_change)
        btnHome.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, MenuActivity::class.java))
            activity?.finish()
        }
        btnInfo.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, RulesActivity::class.java))
            activity?.finish()
        }
        btnChange.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, LevelActivity::class.java))
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        musicSet.resume()
    }

    override fun onPause() {
        super.onPause()
        musicSet.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerAnimation.stopTimer(binding)
        musicSet.release()
    }

    override fun onMovePuzzle(move: Int) {
        gameController.moveCount = move
    }
}