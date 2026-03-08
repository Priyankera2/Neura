package com.example.neura

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MemoryTest : AppCompatActivity() {

    private lateinit var buttons: List<Button>
    private lateinit var tvScore: TextView
    private lateinit var btnStart: Button

    private val sequence = mutableListOf<Int>()
    private val userSequence = mutableListOf<Int>()

    private var score = 0
    private var isUserTurn = false
    private var delaySpeed = 800L

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_test)

        tvScore = findViewById(R.id.tvScore)
        btnStart = findViewById(R.id.btnStart)

        buttons = listOf(
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4)
        )

        btnStart.setOnClickListener {
            startGame()
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!isUserTurn) return@setOnClickListener

                animateButton(index)
                userSequence.add(index)
                checkUserInput()
            }
        }
    }

    private fun startGame() {
        handler.removeCallbacksAndMessages(null)

        score = 0
        delaySpeed = 800L
        sequence.clear()
        userSequence.clear()

        tvScore.text = "Score: $score"
        btnStart.isEnabled = false
        btnStart.text = "Playing..."

        nextRound()
    }

    private fun nextRound() {
        handler.removeCallbacksAndMessages(null)

        userSequence.clear()
        sequence.add(Random.nextInt(4))

        if (delaySpeed > 300) {
            delaySpeed -= 30
        }

        showSequence()
    }

    private fun showSequence() {
        isUserTurn = false
        disableButtons()

        sequence.forEachIndexed { i, index ->
            handler.postDelayed({
                animateButton(index)
            }, i * delaySpeed)
        }

        handler.postDelayed({
            isUserTurn = true
            enableButtons()
            Toast.makeText(this, "Your Turn", Toast.LENGTH_SHORT).show()
        }, sequence.size * delaySpeed)
    }

    private fun animateButton(index: Int) {
        val button = buttons[index]
        button.animate().cancel()
        button.animate()
            .alpha(0.3f)
            .setDuration(180)
            .withEndAction {
                button.animate()
                    .alpha(1f)
                    .setDuration(180)
                    .start()
            }
            .start()
    }

    private fun checkUserInput() {
        val currentIndex = userSequence.lastIndex

        if (userSequence[currentIndex] != sequence[currentIndex]) {
            gameOver()
            return
        }

        if (userSequence.size == sequence.size) {
            score++
            tvScore.text = "Score: $score"

            handler.postDelayed({
                nextRound()
            }, 1000)
        }
    }

    private fun gameOver() {
        handler.removeCallbacksAndMessages(null)

        isUserTurn = false
        enableButtons()

        btnStart.isEnabled = true
        btnStart.text = "Restart"


        TestResultStore.memoryScore = score.toFloat()
        startActivity(Intent(this, ReactionTest::class.java))
        finish()
        //Toast.makeText(this, "Game Over! Final Score: $score", Toast.LENGTH_LONG).show()
    }

    private fun disableButtons() {
        buttons.forEach { it.isEnabled = false }
    }

    private fun enableButtons() {
        buttons.forEach { it.isEnabled = true }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}