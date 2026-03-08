package com.example.neura

object CognitiveAnalyzer {

    fun computeCognitiveScore(
        memory: Float,
        reaction: Float,
        speech: Float,
        drawing: Float
    ): Float {

        val memoryWeight = 0.35f
        val reactionWeight = 0.20f
        val speechWeight = 0.25f
        val drawingWeight = 0.20f

        val score =
            memory * memoryWeight +
                    reaction * reactionWeight +
                    speech * speechWeight +
                    drawing * drawingWeight

        return score
    }

    fun getRiskLevel(score: Float): String {

        return when {
            score >= 75 -> "Low Risk"
            score >= 50 -> "Moderate Risk"
            else -> "High Risk"
        }
    }
}