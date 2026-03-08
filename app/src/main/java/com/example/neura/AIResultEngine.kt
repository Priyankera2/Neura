package com.example.neura

object AIResultEngine {

    fun calculateScore(
        memory: Int,
        reactionTime: Long,
        speech: Int,
        drawing: Float
    ): Int {

        val memoryScore = (memory / 15f) * 100
        val reactionScore = (1f - (reactionTime / 1000f)) * 100
        val speechScore = (speech / 20f) * 100
        val drawingScore = drawing * 100

        val finalScore =
            (0.30f * memoryScore) +
                    (0.25f * reactionScore) +
                    (0.25f * speechScore) +
                    (0.20f * drawingScore)

        return finalScore.toInt()
    }

    fun getRiskLevel(score: Int): String {

        return when {

            score >= 80 -> "Healthy"

            score >= 65 -> "Mild Concern"

            score >= 50 -> "Moderate Concern"

            else -> "High Risk"
        }
    }
}