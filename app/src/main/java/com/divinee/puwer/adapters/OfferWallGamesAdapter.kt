package com.divinee.puwer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divinee.puwer.R
import com.divinee.puwer.models.GameOfferWall

class OfferWallGameAdapter(private val onClickList: (GameOfferWall) -> Unit) :
    RecyclerView.Adapter<OfferWallGameViewHolder>() {
    private var listGamesOfferWall: List<GameOfferWall> = emptyList()
    private var layoutId = R.layout.game_offerwall_item
    @SuppressLint("NotifyDataSetChanged")
    fun updateGamesList(newListGames: List<GameOfferWall>) {
        listGamesOfferWall = newListGames
        layoutId = if (newListGames.size > 3) R.layout.game_offerwall_item_grid else R.layout.game_offerwall_item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferWallGameViewHolder =
        OfferWallGameViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        )

    override fun onBindViewHolder(holder: OfferWallGameViewHolder, position: Int) =
        holder.bindItem(listGamesOfferWall[position], onClickList)

    override fun getItemCount(): Int = listGamesOfferWall.size
}

