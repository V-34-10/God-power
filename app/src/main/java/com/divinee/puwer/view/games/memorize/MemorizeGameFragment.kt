package com.divinee.puwer.view.games.memorize

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.divinee.puwer.R
import com.divinee.puwer.databinding.FragmentMemorizeGameBinding
import com.divinee.puwer.view.games.BaseFragment
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogLoseGameMemorize
import com.divinee.puwer.view.games.dialogs.DialogBaseGame.runDialogVictoryGameMemorize
import com.divinee.puwer.view.games.findcards.bonusgame.BonusWheelGame.stringToNumber
import com.divinee.puwer.view.settings.MusicRunner
import com.divinee.puwer.view.settings.MusicSetup

class MemorizeGameFragment : BaseFragment<FragmentMemorizeGameBinding>() {
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
        binding.textBalance.text =
            context?.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)?.getString(
                "balanceScores",
                context?.getString(R.string.text_default_balance)
            )
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
            balanceWhenVictoryGame(binding)
            countDownTimer.cancel()
        } else {
            runDialogLoseGameMemorize(requireContext()) { resetGame() }
            balanceWhenLoseGame(binding)
            countDownTimer.cancel()
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
                balanceWhenLoseGame(binding)
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

    override fun observeControlBarGame() {
        super.observeControlBarGame()
        for (i in 0..2) {
            binding.blockSetCoins.getChildAt(i).setOnClickListener {
                setCoinToPlaceholder(i)
            }
        }
    }

    private fun balanceWhenVictoryGame(binding: FragmentMemorizeGameBinding) =
        updateBalance(binding, 200)

    private fun balanceWhenLoseGame(binding: FragmentMemorizeGameBinding) =
        updateBalance(binding, -200)

    private fun updateBalance(binding: FragmentMemorizeGameBinding, amount: Int) {
        val currentBalanceText = binding.textBalance.text?.toString()
        val currentBalance = currentBalanceText?.let { stringToNumber(it) } ?: 0
        val newBalance = (currentBalance + amount).coerceAtLeast(0)
        binding.textBalance.text = newBalance.toString()
        context?.getSharedPreferences("PrefDivinePower", MODE_PRIVATE)?.edit()
            ?.putString("balanceScores", newBalance.toString())?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}