package com.example.neura

object ClockTaskGenerator {

    var hour = 0
    var minute = 0

    private val possibleTimes = listOf(
        Pair(11,10),
        Pair(3,45),
        Pair(8,20),
        Pair(6,30),
        Pair(1,50),
        Pair(10,25)
    )

    fun generate() {
        val t = possibleTimes.random()
        hour = t.first
        minute = t.second
    }

    fun getTimeString(): String {
        return "%02d:%02d".format(hour, minute)
    }
}