package com.example.neura

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*
import kotlin.math.max
import java.util.Locale



class SpeechTest : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private val animalList = mutableSetOf<String>()
    private var isTesting = false

    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var btnAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_test)

        tvTimer = findViewById(R.id.tv_timer)
        tvScore = findViewById(R.id.tv_score)
        btnAction = findViewById(R.id.btn_action)

        checkPermissions()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        btnAction.setOnClickListener {
            if (!isTesting) {
                startTest()
            }
        }
    }

    private fun startTest() {

        isTesting = true
        animalList.clear()

        tvScore.text = "Score: 0"
        tvTimer.text = "60s"

        btnAction.isEnabled = false
        btnAction.text = "Listening..."

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {

            override fun onResults(results: Bundle?) {
                val matches =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                matches?.get(0)?.let {
                    processSpeech(it)
                }

                if (isTesting) {
                    speechRecognizer.startListening(intent)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                matches?.get(0)?.let {
                    processSpeech(it)
                }
            }

            override fun onError(error: Int) {
                if (isTesting) {
                    speechRecognizer.startListening(intent)
                }
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)

        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                stopTest()
            }

        }.start()
    }

    private fun processSpeech(text: String) {

        text.split(" ").forEach { word ->

            val cleanWord = word.lowercase().trim()

            if (AnimalData.dictionary.contains(cleanWord) &&
                !animalList.contains(cleanWord)
            ) {
                animalList.add(cleanWord)
                updateScoreUI()
            }
        }
    }

    private fun updateScoreUI() {

        tvScore.text = "Score: ${animalList.size}"

        tvScore.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(100)
            .withEndAction {
                tvScore.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
            }
    }

    private fun stopTest() {

        isTesting = false
        speechRecognizer.stopListening()

        btnAction.isEnabled = true
        btnAction.text = "Restart"

        val finalScore = animalList.size

        showResultDialog(finalScore)
        TestResultStore.speechScore = finalScore.toFloat()
        startActivity(Intent(this, DrawingTest::class.java))
        finish()
    }

    private fun showResultDialog(score: Int) {

        val message =
            if (score < 15)
                "Score: $score. Consider consulting a specialist."
            else
                "Score: $score. Great job!"

        android.app.AlertDialog.Builder(this)
            .setTitle("Test Complete")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}