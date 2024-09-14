package com.divinee.puwer.view.games

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.divinee.puwer.R
import com.divinee.puwer.view.games.findcards.FindCardsGameFragment
import com.divinee.puwer.view.games.memorize.MemorizeGameFragment
import com.divinee.puwer.view.games.puzzle.PuzzleGameFragment

object FragmentsManagerNavigation {

    fun newFragment(game: String?, context: Context): Fragment {
        return when (game) {
            context.getString(R.string.first_game_btn) -> MemorizeGameFragment()
            context.getString(R.string.second_game_btn) -> FindCardsGameFragment()
            else -> PuzzleGameFragment()
        }
    }

    fun removeFragment(activity: AppCompatActivity, fragment: Fragment, containerId: Int) {
        activity.supportFragmentManager.commit {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }

    fun backStack(activity: AppCompatActivity): Boolean {
        return if (activity.supportFragmentManager.backStackEntryCount > 0) {
            activity.supportFragmentManager.popBackStack()
            true
        } else false
    }
}