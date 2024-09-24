package com.divinee.puwer.view.games.findcards

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.divinee.puwer.adapters.FindCardAdapter
import com.divinee.puwer.models.FindCard

class FindPairAdapterManager(
    private val bindingSetup: BindingSetup
) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FindCardAdapter

    fun initRecyclerView(context: Context) {
        recyclerView = bindingSetup.setupRecyclerView(context)
    }

    fun setupAdapter(cardList: List<FindCard>) {
        adapter = FindCardAdapter(cardList).also {
            recyclerView.adapter = it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetAdapter() = adapter.notifyDataSetChanged()

    fun notifyItemChanged(position: Int) = adapter.notifyItemChanged(position)

    fun setOnItemClickListener(onClick: (FindCard, Int) -> Unit) {
        adapter.onFindCardClick = onClick
    }
}