package com.divinee.puwer.models

data class FindCard(
    val picture: Int,
    var flipItem: Boolean = false,
    var matchItem: Boolean = false,
    var positionItem: Int = -1
)