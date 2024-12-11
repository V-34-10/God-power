package com.divinee.puwer.view.menu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivityMenuBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.models.GameOfferWall
import com.divinee.puwer.view.BaseActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup
import com.divinee.puwer.view.settings.SettingsActivity
import kotlin.system.exitProcess

class MenuActivity : BaseActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var configModes: ConfigModesApp

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
        musicSet = MusicSetup(this)
        configModes = ConfigModesApp(this, binding, this@MenuActivity)
        MusicRunner.soundStartMode(this, R.raw.sound__menu, musicSet)
        checkOfferWallAttributes()
        menuClickButtons()
    }

    private fun checkOfferWallAttributes() {
        val offerWallStatus =
            getSharedPreferences("PrefDivinePower", MODE_PRIVATE).getBoolean("StatusOffer", false)
        val listGamesOffer =
            intent.getParcelableArrayListExtra<GameOfferWall>("listGamesOffer") ?: emptyList()

        when {
            offerWallStatus && listGamesOffer.isNotEmpty() && listGamesOffer.any {
                it.menuLabel.startsWith(
                    "https://"
                )
            } -> configModes.startWorkMode(listGamesOffer)

            offerWallStatus && listGamesOffer.isNotEmpty() -> {
                configModes.startDemoModeApp(listGamesOffer)
            }

            else -> {
                binding.controlButton.visibility = View.VISIBLE
                binding.listButtonsGames.visibility = View.VISIBLE
            }
        }
    }

    private fun menuClickButtons() {
        binding.apply {
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
            finish()
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
            finish()
        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun allFinish() {
        exitProcess(0)
    }
}