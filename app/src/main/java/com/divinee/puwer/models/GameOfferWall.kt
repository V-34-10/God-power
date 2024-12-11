package com.divinee.puwer.models

import android.os.Parcel
import android.os.Parcelable

data class GameOfferWall(
    val itemIndex: Int? = 0,
    val menuLabel: String = "",
    val imageUrl: String = "",
    val backupVisual: String = "",
    val actionPrompt: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(GameOfferWall::class.java.classLoader) as? Int,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(itemIndex)
        parcel.writeString(menuLabel)
        parcel.writeString(imageUrl)
        parcel.writeString(backupVisual)
        parcel.writeString(actionPrompt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GameOfferWall> {

        override fun createFromParcel(parcel: Parcel): GameOfferWall = GameOfferWall(parcel)

        override fun newArray(size: Int): Array<GameOfferWall?> = arrayOfNulls(size)
    }
}