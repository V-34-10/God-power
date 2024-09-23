package com.divinee.puwer.view.rules

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityRulesBinding
import com.divinee.puwer.view.games.SceneActivity
import com.divinee.puwer.view.levels.LevelActivity
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class RulesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulesBinding.inflate(layoutInflater) }
    private lateinit var musicSet: MusicSetup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        musicSet = MusicSetup(this)
        MusicRunner.soundStartMode(this, R.raw.sound__menu, musicSet)
        viewPulesForGameName()
        observeButtonRules()
    }

    private fun viewPulesForGameName() {
        when (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
            .getString("nameGame", this.getString(R.string.first_game_btn))) {
            getString(R.string.first_game_btn) -> binding.textRules.setText(R.string.text_rules_first_game)
            getString(R.string.second_game_btn) -> binding.textRules.setText(R.string.text_rules_second_game)
            getString(R.string.three_game_btn) -> binding.textRules.setText(R.string.text_rules_three_game)
        }
    }

    private fun observeButtonRules() {
        val onClickListener = View.OnClickListener { view ->
            view.startAnimation(startAnimation(this@RulesActivity))
            when (view.id) {
                R.id.btn_play_rules -> {
                    when (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                        .getString("nameGame", this.getString(R.string.first_game_btn))) {
                        getString(R.string.three_game_btn) -> startActivity(
                            Intent(
                                this@RulesActivity,
                                LevelActivity::class.java
                            )
                        )

                        else -> startActivity(Intent(this@RulesActivity, SceneActivity::class.java))
                    }
                }

                R.id.btn_home -> goToMenu()
            }
        }
        binding.btnPlayRules.setOnClickListener(onClickListener)
        binding.btnHome.setOnClickListener(onClickListener)
    }

    private fun goToMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        goToMenu()
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