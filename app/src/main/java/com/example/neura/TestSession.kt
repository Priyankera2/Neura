package com.example.neura



data class TestSession(

    val memoryScore: Float = 0f,
    val reactionScore: Float = 0f,
    val speechScore: Float = 0f,
    val drawingScore: Float = 0f,
    val orientationScore: Float = 0f,

    val finalScore: Float = 0f,
    val riskLevel: String = "",

    val timestamp: Long = System.currentTimeMillis()
)