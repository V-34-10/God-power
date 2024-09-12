package com.divinee.puwer.view.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityMenuBinding
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.SettingsActivity
import com.divinee.puwer.view.welcome.MainActivity

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        menuClickButtons()
    }

    private fun menuClickButtons() {
        binding.apply {
            templateClickButton(
                btnHome,
                MainActivity::class.java,
                startAnimation(this@MenuActivity)
            )
            templateClickButton(
                btnSettings,
                SettingsActivity::class.java,
                startAnimation(this@MenuActivity)
            )
            templateClickGameButton(
                btnFirstGame,
                R.string.first_game_btn,
                startAnimation(this@MenuActivity)
            )
            templateClickGameButton(
                btnSecondGame,
                R.string.second_game_btn,
                startAnimation(this@MenuActivity)
            )
            templateClickGameButton(
                btnThreeGame,
                R.string.three_game_btn,
                startAnimation(this@MenuActivity)
            )
        }
    }

    private fun templateClickGameButton(button: View, gameName: Int, animation: Animation) {
        button.setOnClickListener {
            it.startAnimation(animation)
            this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                .putString("nameGame", this.getString(gameName)).apply()
            startActivity(Intent(this@MenuActivity, RulesActivity::class.java))
        }
    }

    private fun <T : AppCompatActivity> templateClickButton(
        button: View,
        activityClass: Class<T>,
        animation: Animation
    ) {
        button.setOnClickListener {
            it.startAnimation(animation)
            startActivity(Intent(this@MenuActivity, activityClass))
        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }
}