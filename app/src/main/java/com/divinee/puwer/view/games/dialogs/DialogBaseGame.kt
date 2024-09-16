package com.divinee.puwer.view.games.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.divinee.puwer.R

object DialogBaseGame {

    fun runDialogLoseGame(context: Context) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_game_lose)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

        val btnRestart = dialog.findViewById<ImageView>(R.id.btn_restart_dialog)
        btnRestart.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun runDialogVictoryGame(context: Context) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_game_victory)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

        val btnRestart = dialog.findViewById<ImageView>(R.id.btn_next_dialog)
        btnRestart.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}