package com.divinee.puwer.view.games.memorize

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.divinee.puwer.R
import com.divinee.puwer.animation.AnimationSetup.startAnimation
import com.divinee.puwer.databinding.FragmentMemorizeGameBinding
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogLoseGameMemorize
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogVictoryGameMemorize
import com.divinee.puwer.view.menu.MenuActivity
import com.divinee.puwer.view.rules.RulesActivity
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class MemorizeGameFragment : Fragment() {
    private lateinit var binding: FragmentMemorizeGameBinding
    private lateinit var musicSet: MusicSetup
    private lateinit var coinPlaceholders: List<ImageView>
    private lateinit var coinsSet: List<ImageView>
    private var coinImages = listOf(
        R.drawable.memorize_1,
        R.drawable.memorize_2,
        R.drawable.memorize_3
    )
    private var coinImagesSet = listOf(
        R.drawable.memorize_1a,
        R.drawable.memorize_2a,
        R.drawable.memorize_3a
    )
    private var selectedCoins = mutableListOf<Int>()
    private lateinit var correctCombination: List<Int>
    private val choiceCoins = mutableListOf<ImageView>()
    private var selectedCoinIndex = -1
    private var selectedChoiceCoinIndex = -1
    private var isGameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var timeRemaining = 60000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemorizeGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicSet = MusicSetup(requireContext())
        MusicRunner.musicStartMode(requireContext(), R.raw.music__memorize, musicSet)

        coinPlaceholders = listOf(
            binding.coinPlaceholder1,
            binding.coinPlaceholder2,
            binding.coinPlaceholder3
        )
        coinsSet = listOf(
            binding.coinItem1,
            binding.coinItem2,
            binding.coinItem3
        )

        startGame()
        binding.btnNext.setOnClickListener {
            if (isGameStarted) {
                checkAnswer()
            }
        }
        choiceCoins.addAll(listOf(binding.coinChoice1, binding.coinChoice2, binding.coinChoice3))
        for (i in 0..2) {
            choiceCoins[i].setOnClickListener { onChoiceCoinClick(i) }
        }
        observeControlBarGame()
    }

    private fun onChoiceCoinClick(index: Int) {
        if (!isGameStarted) return
        selectedChoiceCoinIndex = index
        for (i in 0..2) {
            choiceCoins[i].alpha = if (i == selectedChoiceCoinIndex) 1.0f else 0.5f
        }
    }

    private fun setCoinToPlaceholder(placeholderIndex: Int) {
        if (selectedChoiceCoinIndex == -1) return
        val selectedCoinDrawable = coinImagesSet[selectedChoiceCoinIndex]
        coinsSet[placeholderIndex].setImageResource(selectedCoinDrawable)
        selectedCoins.add(selectedChoiceCoinIndex)
        choiceCoins[selectedChoiceCoinIndex].visibility = View.GONE
        selectedChoiceCoinIndex = -1
        for (i in 0..2) {
            choiceCoins[i].alpha = 1.0f
        }

        when (selectedCoins.size) {
            1 -> binding.statusGame.setImageResource(R.drawable.status_win_one_items_memorize_game)
            2 -> binding.statusGame.setImageResource(R.drawable.status_win_two_items_memorize_game)
            3 -> binding.statusGame.setImageResource(R.drawable.status_win_memorize_game)
        }

        if (selectedCoins.size == 3) {
            checkAnswer()
        }
    }

    private fun startGame() {
        isGameStarted = true
        selectedCoinIndex = -1
        selectedCoins.clear()

        binding.blockCombinationForMemories.visibility = View.VISIBLE
        binding.statusTime.visibility = View.GONE
        binding.blockSetCoins.visibility = View.GONE
        binding.coinItem1.setImageResource(R.drawable.memorize_0)
        binding.coinItem2.setImageResource(R.drawable.memorize_0)
        binding.coinItem3.setImageResource(R.drawable.memorize_0)
        binding.blockVariantsCoinsForChoice.visibility = View.GONE

        generateCombination()
        showCombination()
        startTimer()

        Handler().postDelayed({
            hideCombinationAndShowChoiceBlocks()
        }, 3000)
    }

    private fun hideCombinationAndShowChoiceBlocks() {
        binding.blockCombinationForMemories.visibility = View.GONE
        binding.statusTime.visibility = View.VISIBLE
        binding.blockSetCoins.visibility = View.VISIBLE
        binding.blockVariantsCoinsForChoice.visibility = View.VISIBLE
        binding.btnNext.visibility = View.VISIBLE

        for (i in 0..2) {
            choiceCoins[i].visibility = View.VISIBLE
        }
    }

    private fun generateCombination() {
        correctCombination = coinImages.shuffled().take(3).map { coinImages.indexOf(it) }
    }

    private fun showCombination() {
        for (i in 0..2) {
            coinPlaceholders[i].setImageResource(coinImages[correctCombination[i]])
        }
    }

    private fun checkAnswer() {
        if (coinsSet.size < 3) {
            Toast.makeText(requireContext(), "Place all coins!", Toast.LENGTH_SHORT).show()
            return
        }

        val userCombination = selectedCoins
        if (userCombination == correctCombination) {
            runDialogVictoryGameMemorize(requireContext()) { resetGame() }
        } else {
            runDialogLoseGameMemorize(requireContext()) { resetGame() }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerTextView()
            }

            override fun onFinish() {
                runDialogLoseGameMemorize(requireContext()) { resetGame() }
                resetGame()
            }
        }.start()
    }

    @SuppressLint("DefaultLocale")
    private fun updateTimerTextView() {
        val minutes = (timeRemaining / 1000).toInt() / 60
        val seconds = (timeRemaining / 1000).toInt() % 60
        binding.textTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    fun resetGame() {
        isGameStarted = false
        countDownTimer.cancel()
        timeRemaining = 60000L
        updateTimerTextView()

        selectedCoins.clear()
        binding.statusGame.setImageResource(R.drawable.status_default_win_memorize_game)
        binding.btnNext.visibility = View.GONE

        for (i in 0..2) {
            choiceCoins[i].visibility = View.VISIBLE
            choiceCoins[i].alpha = 1.0f
        }

        for (i in 0..2) {
            coinsSet[i].setImageDrawable(null)
        }
        startGame()
    }

    private fun observeControlBarGame() {
        val btnHome = requireView().findViewById<View>(R.id.btn_home)
        val btnInfo = requireView().findViewById<View>(R.id.btn_info)
        btnHome.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, MenuActivity::class.java))
            activity?.finish()
        }
        btnInfo.setOnClickListener {
            it.startAnimation(startAnimation(requireContext()))
            startActivity(Intent(context, RulesActivity::class.java))
            activity?.finish()
        }
        for (i in 0..2) {
            binding.blockSetCoins.getChildAt(i).setOnClickListener {
                setCoinToPlaceholder(i)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        musicSet.resume()
    }

    override fun onPause() {
        super.onPause()
        musicSet.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        musicSet.release()
    }
}