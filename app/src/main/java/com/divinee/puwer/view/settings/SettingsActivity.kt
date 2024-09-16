package com.divinee.puwer.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.ActivitySettingsBinding
import com.divinee.puwer.view.menu.MenuActivity

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private lateinit var musicVolume: VolumeManager
    private lateinit var soundVolume: VolumeManager
    private lateinit var musicSet: MusicSetup

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        musicVolume = VolumeManager(this)
        soundVolume = VolumeManager(this)
        musicSet = MusicSetup(this)
        MusicRunner.soundStartMode(this, R.raw.sound_menu, musicSet)
        updateBackgroundSoundAndMusic()
        settingsClickButtons()
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
                        .putString("balanceScore", this.getString(R.string.text_default_balance))
                        .apply()
                }

                R.id.btn_home, R.id.btn_play_settings -> {
                    startActivity(Intent(this@SettingsActivity, MenuActivity::class.java))
                }
            }
        }

        binding.textResetScore.setOnClickListener(onClickListener)
        binding.btnSwitchMusic.setOnClickListener {
            val musicStatus = this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getBoolean("musicStatus", false)

            if (musicStatus) {
                musicVolume.setOffVolume()
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("musicStatus", false).apply()
                binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_off_btn)
            } else {
                musicVolume.setOnVolume()
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("musicStatus", true).apply()
                binding.btnSwitchMusic.setBackgroundResource(R.drawable.switch_on_btn)
            }
        }
        binding.btnSwitchSound.setOnClickListener {
            val soundStatus = this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)
                .getBoolean("soundStatus", false)

            if (soundStatus) {
                soundVolume.setOnVolume()
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("soundStatus", false).apply()
                binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_off_btn)
            } else {
                soundVolume.setOffVolume()
                this.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putBoolean("soundStatus", true).apply()
                binding.btnSwitchSound.setBackgroundResource(R.drawable.switch_on_btn)
            }
        }
        binding.btnHome.setOnClickListener(onClickListener)
        binding.btnPlaySettings.setOnClickListener(onClickListener)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() = super.onBackPressed()

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