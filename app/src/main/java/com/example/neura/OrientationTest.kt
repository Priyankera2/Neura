package com.example.neura

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class OrientationTest : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var answerInput: EditText
    private lateinit var nextButton: Button

    private var score = 0
    private var index = 0

    private val questions = listOf(
        "What year is it?",
        "What month is it?",
        "What day of the week is it?",
        "What country are you in?",
        "What city are you in?"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_orientation_test)

        questionText = findViewById(R.id.questionText)
        answerInput = findViewById(R.id.answerInput)
        nextButton = findViewById(R.id.nextButton)

        showQuestion()

        nextButton.setOnClickListener {

            checkAnswer()

            index++

            if(index < questions.size){

                showQuestion()

            }else{

                TestResultStore.orientationScore = score.toFloat()

                startActivity(
                    Intent(this, ResultActivity::class.java)
                )

                finish()
            }
        }
    }

    private fun showQuestion(){

        questionText.text = questions[index]

        answerInput.setText("")
    }

    private fun checkAnswer(){

        val answer = answerInput.text.toString().lowercase()

        val calendar = Calendar.getInstance()

        when(index){

            0 -> if(answer.contains(calendar.get(Calendar.YEAR).toString()))
                score += 2

            1 -> if(answer.contains(
                    calendar.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.getDefault()
                    )!!.lowercase()))
                score += 2

            2 -> if(answer.contains(
                    calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.LONG,
                        Locale.getDefault()
                    )!!.lowercase()))
                score += 2

            3 -> if(answer.contains("india"))
                score += 2

            4 -> if(answer.contains("punjab"))
                score += 2
        }
    }
}