package com.divinee.puwer.view.levels

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityLevelBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.view.BaseActivity
import com.divinee.puwer.view.games.SceneActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class LevelActivity : BaseActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
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
}