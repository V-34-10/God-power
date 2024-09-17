package com.divinee.puwer.view.games.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.divinee.puwer.R
import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentPuzzleGameBinding
import com.divinee.puwer.view.games.findcards.BindingSetup
import com.divinee.puwer.view.games.findcards.FindPairGameManager
import com.divinee.puwer.view.games.puzzle.GameController.restartGame

object DialogBaseGame {

    private fun createDialog(context: Context, layoutResId: Int): Dialog {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(layoutResId)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    private fun showDialog(
        context: Context,
        layoutResId: Int,
        buttonId: Int,
        onDismiss: () -> Unit
    ) {
        val dialog = createDialog(context, layoutResId)
        val button = dialog.findViewById<ImageView>(buttonId)
        button.setOnClickListener {
            dialog.dismiss()
            onDismiss()
        }
        dialog.show()
    }

    fun runDialogLoseGame(context: Context, gameManager: FindPairGameManager?) {
        showDialog(context, R.layout.dialog_game_lose, R.id.btn_restart_dialog) {
            gameManager?.resetGame()
        }
    }

    fun runDialogVictoryGame(
        context: Context,
        gameManager: FindPairGameManager?,
        bindingSetup: BindingSetup?
    ) {
        showDialog(context, R.layout.dialog_game_victory, R.id.btn_next_dialog) {
            gameManager?.resetGame()
            bindingSetup?.observeButtonBonusGame(context)
        }
    }

    fun runDialogLoseGamePuzzle(
        context: Context,
        timerAnimation: TimerAnimation,
        binding: FragmentPuzzleGameBinding,
        selectLevel: String
    ) {
        showDialog(context, R.layout.dialog_game_lose, R.id.btn_restart_dialog) {
            restartGame(timerAnimation, binding, selectLevel, context)
        }
    }

    fun runDialogVictoryGamePuzzle(
        context: Context,
        timerAnimation: TimerAnimation,
        binding: FragmentPuzzleGameBinding,
        selectLevel: String
    ) {
        showDialog(context, R.layout.dialog_game_victory, R.id.btn_next_dialog) {
            restartGame(timerAnimation, binding, selectLevel, context)
        }
    }

    fun runDialogLoseGameMemorize(context: Context) {
        showDialog(context, R.layout.dialog_game_lose, R.id.btn_restart_dialog) {}
    }

    fun runDialogVictoryGameMemorize(context: Context) {
        showDialog(context, R.layout.dialog_game_victory, R.id.btn_next_dialog) {}
    }
}