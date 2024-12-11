package com.divinee.puwer.view.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.divinee.puwer.R
import com.divinee.puwer.adapters.OfferWallGameAdapter
import com.divinee.puwer.adapters.loadImageBackground
import com.divinee.puwer.databinding.ActivityMenuBinding
import com.divinee.puwer.models.GameOfferWall
import com.divinee.puwer.view.rules.RulesActivity

class ConfigModesApp(
    private val context: Context,
    private val binding: ActivityMenuBinding,
    private val activity: MenuActivity
) {
    private lateinit var adapter: OfferWallGameAdapter
    fun startDemoModeApp(listGamesOffer: List<GameOfferWall>) {
        val imageButtons = listOf(
            binding.btnFirstGame,
            binding.btnSecondGame,
            binding.btnThreeGame
        )
        listGamesOffer.forEachIndexed { i, item ->
            if (i < imageButtons.size) {
                Log.d("MenuActivity", "Games received: gameVisualFile $i ${item.imageUrl}")
                imageButtons[i].loadImageBackground(item.imageUrl)
            } else {
                Log.w("MenuActivity", "More games received than buttons. Skipping index $i")
                return
            }
        }
        binding.controlButton.visibility = View.VISIBLE
        binding.listButtonsGames.visibility = View.VISIBLE
    }

    fun startWorkMode(games: List<GameOfferWall>) {
        binding.backgroundMain.setBackgroundResource(R.drawable.background_menu_activity_blur)
        binding.titleOffers.visibility = View.VISIBLE
        binding.gamesOfferWallList.visibility = View.VISIBLE
        binding.gamesOfferWallList.layoutManager = if (games.size > 3) { GridLayoutManager(context, 2) } else { LinearLayoutManager(context) }
        adapter = OfferWallGameAdapter(this::onGameClick)
        binding.gamesOfferWallList.adapter = adapter
        adapter.updateGamesList(games)

        Log.d("MenuActivity", "Games received:")
        for (game in games) {
            Log.d(
                "MenuActivity",
                "inx: ${game.itemIndex}, title: ${game.menuLabel}, bgUrl: ${game.imageUrl}, fgUrl: ${game.backupVisual}, play: ${game.actionPrompt}"
            )
        }
        Log.d("MenuActivity", "Games received: ${games.size}")
    }

    private fun onGameClick(games: GameOfferWall) {
        if (games.menuLabel.startsWith("https://")) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(games.menuLabel)))
        } else {
            val gameName = when (games.itemIndex) {
                0 -> activity.getString(R.string.first_game_btn)
                1 -> activity.getString(R.string.second_game_btn)
                2 -> activity.getString(R.string.three_game_btn)
                else -> null
            }
            gameName?.let {
                activity.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
                    .putString("nameGame", it).apply()
            }
            activity.startActivity(Intent(activity, RulesActivity::class.java))
            activity.finish()
        }
    }
}