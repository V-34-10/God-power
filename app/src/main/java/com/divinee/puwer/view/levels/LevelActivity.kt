package com.divinee.puwer.view.levels

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityLevelBinding
import com.divinee.puwer.view.games.SceneActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class LevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
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
        saveLevelGamePuzzle()
    }

    private fun saveLevelGamePuzzle() {
        val onClickListener = View.OnClickListener { view ->
            view.startAnimation(startAnimation(this@LevelActivity))
            when (view.id) {
                R.id.button_level_easy -> {
                    this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                        .putString("levelGame", this.getString(R.string.easy_level_btn)).apply()
                    startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
                }

                R.id.button_level_medium -> {
                    this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                        .putString("levelGame", this.getString(R.string.medium_level_btn)).apply()
                    startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
                }

                R.id.button_level_hard -> {
                    this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                        .putString("levelGame", this.getString(R.string.hard_level_btn)).apply()
                    startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
                }

                R.id.btn_home -> goToRules()
            }
        }
        binding.buttonLevelEasy.setOnClickListener(onClickListener)
        binding.buttonLevelMedium.setOnClickListener(onClickListener)
        binding.buttonLevelHard.setOnClickListener(onClickListener)
        binding.btnHome.setOnClickListener(onClickListener)
    }

    private fun goToRules() {
        startActivity(Intent(this, RulesActivity::class.java))
        finish()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        goToRules()
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