package com.example.neura

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveSession(finalScore: Float, riskLevel: String) {

        val user = auth.currentUser ?: return

        val data = hashMapOf(

            "memoryScore" to TestResultStore.memoryScore,

            "reactionScore" to TestResultStore.reactionScore,
            //"reactionErrors" to TestResultStore.reactionErrors,

            "speechScore" to TestResultStore.speechScore,

            "drawingScore" to TestResultStore.drawingScore,

            "orientationScore" to TestResultStore.orientationScore,

            "finalScore" to finalScore,

            "riskLevel" to riskLevel,

            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(user.uid)
            .collection("sessions")
            .add(data)
            .addOnSuccessListener {

                Log.d("Firebase", "Session saved")

            }
            .addOnFailureListener {

                Log.e("Firebase", "Error saving session")

            }
    }
}