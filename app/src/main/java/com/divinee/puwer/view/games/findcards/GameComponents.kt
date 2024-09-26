package com.divinee.puwer.view.games.findcards

import com.divinee.puwer.animation.TimerAnimation
import com.divinee.puwer.databinding.FragmentFindCardsGameBinding

data class GameComponents(
    val timerAnimation: TimerAnimation,
    val bindingSetup: BindingSetup,
    val adapterManager: AdapterManager,
    val pairItemManager: CardItemManager,
    val clickHandler: CardClickHandler
)

fun createGameComponents(binding: FragmentFindCardsGameBinding): GameComponents {
    val timerAnimation = TimerAnimation()
    val bindingSetup = BindingSetup(binding)
    val adapterManager = AdapterManager(bindingSetup)
    val pairItemManager = CardItemManager()
    val clickHandler = CardClickHandler(
        adapterManager,
        pairItemManager,
        timerAnimation,
        bindingSetup
    )
    return GameComponents(
        timerAnimation,
        bindingSetup,
        adapterManager,
        pairItemManager,
        clickHandler
    )
}