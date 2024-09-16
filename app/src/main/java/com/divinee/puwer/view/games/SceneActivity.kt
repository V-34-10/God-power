package com.divinee.puwer.view.games

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.databinding.ActivitySceneBinding
import com.divinee.puwer.view.games.FragmentsManagerNavigation.backStack
import com.divinee.puwer.view.games.FragmentsManagerNavigation.newFragment
import com.divinee.puwer.view.games.FragmentsManagerNavigation.removeFragment
import com.divinee.puwer.view.menu.MenuActivity

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScene)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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