package com.divinee.puwer.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.divinee.puwer.R
import com.divinee.puwer.models.GameOfferWall

class OfferWallGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageUrl: ImageView = itemView.findViewById(R.id.background_block_game)
    private val backupVisual: ImageView = itemView.findViewById(R.id.img_game)
    private val actionPrompt: Button = itemView.findViewById(R.id.start_btn_game)

    fun bindItem(game: GameOfferWall, onGameClick: (GameOfferWall) -> Unit) {
        Glide.with(itemView.context).load(game.imageUrl).into(imageUrl)
        Glide.with(itemView.context).load(game.backupVisual).into(backupVisual)
        actionPrompt.text = game.actionPrompt
        actionPrompt.setOnClickListener { onGameClick(game) }
    }
}

fun ImageView.loadImageBackground(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(getBasicBackImage(this.id))
        .error(getBasicBackImage(this.id))
        .into(this)
}

@DrawableRes
private fun getBasicBackImage(@IdRes imageViewId: Int): Int {
    return when (imageViewId) {
        R.id.btn_first_game -> R.drawable.first_game_btn
        R.id.btn_second_game -> R.drawable.second_game_btn
        R.id.btn_three_game -> R.drawable.three_game_btn
        else -> R.drawable.first_game_btn
    }
}