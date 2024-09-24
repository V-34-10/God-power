package com.divinee.puwer.view.games

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicSetup

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected lateinit var binding: T
    protected lateinit var musicSet: MusicSetup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicSet = MusicSetup(requireContext())
        observeControlBarGame()
    }

    open fun observeControlBarGame() {
        val btnHome = requireView().findViewById<View>(R.id.btn_home)
        val btnInfo = requireView().findViewById<View>(R.id.btn_info)

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
        musicSet.release()
    }
}