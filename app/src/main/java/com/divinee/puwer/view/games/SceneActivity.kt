package com.divinee.puwer.view.games

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.divinee.puwer.R
import com.divinee.puwer.databinding.ActivitySceneBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.view.games.FragmentsManagerNavigation.backStack
import com.divinee.puwer.view.games.FragmentsManagerNavigation.newFragment
import com.divinee.puwer.view.games.FragmentsManagerNavigation.removeFragment
import com.divinee.puwer.view.menu.MenuActivity

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
        setFragmentGame()
    }

    private fun setFragmentGame() = removeFragment(
        this, newFragment(
            this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getString("nameGame", this.getString(R.string.first_game_btn)), this
        ), R.id.container_fragments
    )

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!backStack(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }
}