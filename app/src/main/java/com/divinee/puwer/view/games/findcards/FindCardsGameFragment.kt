package com.divinee.puwer.view.games.findcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.view.games.BaseFragment
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class FindCardsGameFragment : BaseFragment<FragmentFindCardsGameBinding>() {
    private lateinit var gameManager: CardGameManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindCardsGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicSet = MusicSetup(requireContext())
        MusicRunner.musicStartMode(requireContext(), R.raw.music__find_cards, musicSet)
        context?.let { gameManager = CardGameManager(it, binding) }
        binding.textBalance.text =
            context?.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)?.getString(
                "balanceScores",
                context?.getString(R.string.text_default_balance)
            )
        gameManager.initGame()
        observeControlBarGame()
    }

    override fun observeControlBarGame() {
        super.observeControlBarGame()
        val btnNext = requireView().findViewById<View>(R.id.btn_next)
        btnNext.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            gameManager.resetGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameManager.destroyGame()
    }
}