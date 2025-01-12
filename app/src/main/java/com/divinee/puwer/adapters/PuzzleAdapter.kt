package com.divinee.puwer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.divinee.puwer.R
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.models.Puzzle
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogVictoryGamePuzzle
import com.divinee.puwer.view.games.findcards.bonusgame.BonusWheelGame.stringToNumber
import com.divinee.puwer.view.games.puzzle.PuzzleHelper
import com.divinee.puwer.view.games.puzzle.PuzzleImageSetup

interface MovePuzzleListener {
    fun onMovePuzzle(move: Int)
}

class PuzzleAdapter(
    recyclerView: RecyclerView,
    private val cardList: MutableList<Puzzle>,
    private val selectedLevel: String,
    private val context: Context,
    private val timerAnimation: TimerAnimation,
    private val binding: FragmentPuzzleGameBinding,
) : RecyclerView.Adapter<PuzzleAdapter.ViewHolder>() {

    private val puzzleImageSetup = PuzzleImageSetup(context)
    private var emptyPosition: Int =
        cardList.indexOfFirst { it.image == R.drawable.puzzle_easy_8 }

    private val levelConfig = puzzleImageSetup.getLevelConfig(selectedLevel)
    private val winListPuzzle: List<Int> = levelConfig.winListPuzzle
    private var timerStarted = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_puzzle, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(cardList[position])

    override fun getItemCount(): Int = cardList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgPuzzle)
        fun bind(puzzle: Puzzle) {
            imageView.setImageResource(puzzle.image)
            itemView.setOnClickListener {
                if (PuzzleHelper.canMoveStepGame(
                        adapterPosition,
                        emptyPosition,
                        levelConfig.spanPuzzle
                    )
                ) {
                    swapPuzzles(adapterPosition, emptyPosition)
                    PuzzleHelper.animatedPuzzle(itemView)
                    emptyPosition = adapterPosition
                }
                if (!timerStarted) {
                    timerAnimation.startTimer(binding, context, null)
                    timerStarted = true
                }
            }
        }
    }

    init {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (PuzzleHelper.canMoveStepGame(
                        viewHolder.adapterPosition,
                        emptyPosition,
                        levelConfig.spanPuzzle
                    ) &&
                    PuzzleHelper.canMoveStepGame(
                        target.adapterPosition,
                        emptyPosition,
                        levelConfig.spanPuzzle
                    )
                ) {
                    swapPuzzles(viewHolder.adapterPosition, target.adapterPosition)
                    emptyPosition = target.adapterPosition
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun swapPuzzles(fromPosition: Int, toPosition: Int) {
        PuzzleHelper.swapCardsGame(cardList, fromPosition, toPosition)

        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        if (PuzzleHelper.checkCard(cardList, winListPuzzle)) {
            runDialogVictoryGamePuzzle(
                context,
                timerAnimation,
                binding,
                selectedLevel
            )
            timerAnimation.stopTimer(binding)
            balanceWhenVictoryGame(binding)
        }
    }

    private fun balanceWhenVictoryGame(binding: FragmentPuzzleGameBinding) {
        var balance = stringToNumber(binding.textBalance.text.toString()) ?: 0
        balance += 200
        binding.textBalance.text = balance.toString()
        context.getSharedPreferences("PrefDivinePower", MODE_PRIVATE).edit()
            .putString("balanceScores", balance.toString()).apply()
    }
}