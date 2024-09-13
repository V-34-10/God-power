package com.divinee.puwer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.divinee.puwer.R
import com.divinee.puwer.models.FindCard

class FindCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pairImage: ImageView = itemView.findViewById(R.id.find_card_item)
}

class FindCardAdapter(
    private val findCardList: List<FindCard>
) :
    RecyclerView.Adapter<FindCardViewHolder>() {
    var onFindCardClick: ((FindCard, Int) -> Unit)? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_find_card, parent, false)
        return FindCardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FindCardViewHolder, position: Int) {
        holder.pairImage.setImageResource(if (findCardList[position].flipItem) findCardList[position].picture else R.drawable.find_0)
        holder.itemView.setOnClickListener {
            onFindCardClick?.invoke(
                findCardList[position],
                position
            )
        }
    }

    override fun getItemCount(): Int = findCardList.size
}