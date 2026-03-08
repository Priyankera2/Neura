package com.example.neura



object TestResultStore {

    var memoryScore = 0f
    var reactionScore = 0f
    var speechScore = 0f
    var drawingScore = 0f
    var orientationScore = 0f

    fun reset() {
        memoryScore = 0f
        reactionScore = 0f
        speechScore = 0f
        drawingScore = 0f
        orientationScore = 0f
    }
}