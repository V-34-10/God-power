package com.divinee.puwer.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameOfferWall(
    val itemIndex: Int? = 0,
    val menuLabel: String = "",
    val imageUrl: String = "",
    val backupVisual: String = "",
    val actionPrompt: String = "",
) : Parcelable