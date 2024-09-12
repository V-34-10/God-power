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

class RulesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulesBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewPulesForGameName()
    }

    private fun viewPulesForGameName() {
        when (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
            .getString("nameGame", this.getString(R.string.first_game_btn))) {
            getString(R.string.first_game_btn) -> binding.textRules.setText(R.string.text_rules_first_game)
            getString(R.string.second_game_btn) -> binding.textRules.setText(R.string.text_rules_second_game)
            getString(R.string.three_game_btn) -> binding.textRules.setText(R.string.text_rules_three_game)
        }

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

                R.id.btn_home -> startActivity(Intent(this@RulesActivity, MenuActivity::class.java))
            }
        }
        binding.btnPlayRules.setOnClickListener(onClickListener)
        binding.btnHome.setOnClickListener(onClickListener)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() = super.onBackPressed()
}