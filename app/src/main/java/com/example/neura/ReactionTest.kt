package com.example.neura

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow



class ReactionTest : AppCompatActivity() {

    private lateinit var stimulusView: View
    private lateinit var tvInstruction: TextView
    private lateinit var btnStart: Button

    private val handler = Handler(Looper.getMainLooper())

    //private val reactionTimes = mutableListOf<Long>()
    //private var startTime: Long = 0
    private var totalTrials = 30
    private var currentTrial = 0
    private var isGreen = false
    private var trialStartTime = 0L

    private val reactionTimes = mutableListOf<Long>()
    private var falsePositives = 0
    private var misses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction_test)

        stimulusView = findViewById(R.id.stimulusView)
        tvInstruction = findViewById(R.id.tvInstruction)
        btnStart = findViewById(R.id.btnStartReaction)

        btnStart.setOnClickListener {
            startTest()
        }

        stimulusView.setOnClickListener {
            handleTap()
        }
    }

    private fun startTest() {
        handler.removeCallbacksAndMessages(null)

        currentTrial = 0
        reactionTimes.clear()
        falsePositives = 0
        misses = 0

        btnStart.isEnabled = false
        tvInstruction.text = "Get Ready..."

        nextTrial()
    }

    private fun nextTrial() {
        if (currentTrial >= totalTrials) {
            finishTest()
            return
        }

        stimulusView.visibility = View.INVISIBLE
        isGreen = false

        val delay = (1500..2500).random().toLong()

        handler.postDelayed({
            showStimulus()
        }, delay)
    }

    private fun showStimulus() {
        stimulusView.visibility = View.VISIBLE
        currentTrial++

        isGreen = (0..9).random() < 7   // 70% chance green

        if (isGreen) {
            setCircleColor(Color.GREEN)
            trialStartTime = System.currentTimeMillis()

            // Miss detection (no tap within 1000ms)
            handler.postDelayed({
                if (isGreen) {
                    misses++
                    isGreen = false
                    nextTrial()
                }
            }, 1000)

        } else {
            setCircleColor(Color.RED)

            handler.postDelayed({
                nextTrial()
            }, 1000)
        }
    }

    private fun handleTap() {
        if (isGreen) {
            val reactionTime = System.currentTimeMillis() - trialStartTime
            reactionTimes.add(reactionTime)
            isGreen = false
            nextTrial()
        } else {
            falsePositives++
        }
    }

    private fun finishTest() {

        handler.removeCallbacksAndMessages(null)

        btnStart.isEnabled = true

        val avgReaction = if (reactionTimes.isNotEmpty()) {
            reactionTimes.average()
        } else 1000.0

        val variance = if (reactionTimes.size > 1) {
            reactionTimes.map { (it - avgReaction).pow(2) }.average()
        } else 0.0

        val accuracy =
            ((totalTrials - falsePositives - misses).toDouble() / totalTrials) * 100

        // ---- AI Reaction Score (0–100) ----

        val speedScore = (1000 - avgReaction).coerceAtLeast(0.0) / 10
        val accuracyScore = accuracy
        val stabilityPenalty = variance / 50

        var reactionScore =
            speedScore * 0.5 +
                    accuracyScore * 0.5 -
                    stabilityPenalty

        reactionScore = reactionScore.coerceIn(0.0, 100.0)

        // IMPORTANT: store score
        TestResultStore.reactionScore = accuracy.toFloat()

        tvInstruction.text = """
        Test Complete
        
        Avg Reaction: ${avgReaction.toInt()} ms
        Variance: ${variance.toInt()}
        False Positives: $falsePositives
        Misses: $misses
        Accuracy: ${accuracy.toInt()}%
        
        Reaction Score: ${reactionScore.toInt()}
    """.trimIndent()

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, SpeechTest::class.java))
            finish()

        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
    private fun setCircleColor(color: Int) {
        val drawable = stimulusView.background.mutate()
        if (drawable is android.graphics.drawable.GradientDrawable) {
            drawable.setColor(color)
        }
    }
}