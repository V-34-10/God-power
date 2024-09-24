package com.divinee.puwer.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivitySettingsBinding
import com.divinee.puwer.decoration.Edge
import com.divinee.puwer.view.BaseActivity
import com.divinee.puwer.view.menu.MenuActivity

class SettingsActivity : BaseActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private lateinit var musicVolume: VolumeManager
    private lateinit var soundVolume: VolumeManager

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Edge.enableEdgeToEdge(this)
        musicVolume = VolumeManager(this)
        soundVolume = VolumeManager(this)
        musicSet = MusicSetup(this)
        MusicRunner.soundStartMode(this, R.raw.sound__menu, musicSet)
        updateBackgroundSoundAndMusic()
        settingsClickButtons()
        observeSwitchMusicButtons()
    }

    private fun updateBackgroundSoundAndMusic() {
        if (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getBoolean("musicStatus", false)
        ) {
            binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_on_btn)
        } else {
            binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_off_btn)
        }
        if (this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getBoolean("soundStatus", false)
        ) {
            binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_on_btn)
        } else {
            binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_off_btn)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun settingsClickButtons() {
        val onClickListener = View.OnClickListener { view ->
            view.startAnimation(startAnimation(this@SettingsActivity))
            when (view.id) {
                R.id.text_reset_score -> {
                    Toast.makeText(applicationContext, R.string.reset_message, Toast.LENGTH_SHORT)
                        .show()
                    this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                        .putString("balanceScores", this.getString(R.string.text_default_balance))
                        .apply()
                }

                R.id.btn_home, R.id.btn_play_settings -> goToMenu()
            }
        }
        binding.textResetScore.setOnClickListener(onClickListener)
        binding.btnHome.setOnClickListener(onClickListener)
        binding.btnPlaySettings.setOnClickListener(onClickListener)
    }

    private fun observeSwitchMusicButtons() {
        val musicStatus = this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
            .getBoolean("musicStatus", false)
        val soundStatus = this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
            .getBoolean("soundStatus", false)
        binding.btnSwitchMusic.setOnClickListener {
            if (musicStatus) {
                musicVolume.setVolume(false)
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("musicStatus", false).apply()
                binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_off_btn)
            } else {
                musicVolume.setVolume(true)
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("musicStatus", true).apply()
                binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_on_btn)
            }
        }
        binding.btnSwitchSound.setOnClickListener {
            if (soundStatus) {
                soundVolume.setVolume(false)
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("soundStatus", false).apply()
                binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_off_btn)
            } else {
                soundVolume.setVolume(true)
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("soundStatus", true).apply()
                binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_on_btn)
            }
        }
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
}