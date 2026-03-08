package com.example.neura

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        val finalScore =
            (TestResultStore.memoryScore +
                    TestResultStore.reactionScore +
                    TestResultStore.speechScore +
                    TestResultStore.drawingScore +
                    TestResultStore.orientationScore) / 5

        val risk = when {
            finalScore > 75 -> "Low Risk"
            finalScore > 50 -> "Moderate Risk"
            else -> "High Risk"
        }
        FirebaseRepository.saveSession(finalScore, risk)
        val session = TestSession(
            memoryScore = TestResultStore.memoryScore,
            reactionScore = TestResultStore.reactionScore,
            speechScore = TestResultStore.speechScore,
            drawingScore = TestResultStore.drawingScore,
            orientationScore = TestResultStore.orientationScore,
            finalScore = finalScore,
            riskLevel = risk
        )

        FirebaseFirestore.getInstance()
            .collection("sessions")
            .add(session)

        showResult(finalScore, risk)
    }

    private fun showResult(score: Float, risk: String){

        AlertDialog.Builder(this)
            .setTitle("Neura Assessment")
            .setMessage(
                "Score: ${String.format("%.1f",score)}\n\nRisk: $risk"
            )
            .setPositiveButton("OK"){_,_-> finish()}
            .show()
    }
}