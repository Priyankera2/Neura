package com.example.neura

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SessionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_detail)

        val memory = intent.getFloatExtra("memory",0f)
        val reaction = intent.getFloatExtra("reaction",0f)
        val speech = intent.getFloatExtra("speech",0f)
        val drawing = intent.getFloatExtra("drawing",0f)
        val orientation = intent.getFloatExtra("orientation",0f)
        val finalScore = intent.getFloatExtra("final",0f)
        val risk = intent.getStringExtra("risk")

        findViewById<TextView>(R.id.tvMemory).text = "Memory Score: ${memory.toInt()}"
        findViewById<TextView>(R.id.tvReaction).text = "Reaction Score: ${reaction.toInt()}"
        findViewById<TextView>(R.id.tvSpeech).text = "Speech Score: ${speech.toInt()}"
        findViewById<TextView>(R.id.tvDrawing).text = "Drawing Score: ${drawing.toInt()}"
        findViewById<TextView>(R.id.tvOrientation).text = "Orientation Score: ${orientation.toInt()}"
        findViewById<TextView>(R.id.tvFinal).text = "Final Score: ${finalScore.toInt()}"
        findViewById<TextView>(R.id.tvRisk).text = "Risk Level: $risk"
    }
}