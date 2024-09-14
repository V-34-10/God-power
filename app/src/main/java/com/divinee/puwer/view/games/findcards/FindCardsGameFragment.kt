package com.divinee.puwer.view.games.findcards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicController
import com.divinee.puwer.view.settings.MusicStart

class FindCardsGameFragment : Fragment() {
    private lateinit var binding: FragmentFindCardsGameBinding
    private lateinit var controllerMusic: MusicController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindCardsGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controllerMusic = MusicController(requireContext())
        MusicStart.musicStartMode(requireContext(), R.raw.music_find_cards, controllerMusic)

        observeControlBarGame()
    }

    private fun observeControlBarGame() {
        val btnHome = requireView().findViewById<View>(R.id.btn_home)
        val btnInfo = requireView().findViewById<View>(R.id.btn_info)
        val btnNext = requireView().findViewById<View>(R.id.btn_next)

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

        btnNext.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))

        }
    }

    override fun onResume() {
        super.onResume()
        controllerMusic.resume()
    }

    override fun onPause() {
        super.onPause()
        controllerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        controllerMusic.release()
    }
}