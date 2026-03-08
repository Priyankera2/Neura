package com.example.neura


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var chartOverall: LineChart
    private lateinit var chartMemory: LineChart
    private lateinit var chartReaction: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        chartOverall = findViewById(R.id.chartOverall)
        chartMemory = findViewById(R.id.chartMemory)
        chartReaction = findViewById(R.id.chartReaction)

        loadData()
    }

    private fun loadData() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("sessions")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->

                val overallEntries = mutableListOf<Entry>()
                val memoryEntries = mutableListOf<Entry>()
                val reactionEntries = mutableListOf<Entry>()

                var index = 0f

                for (doc in result) {

                    val overall = doc.getDouble("finalScore") ?: 0.0
                    val memory = doc.getDouble("memoryScore") ?: 0.0
                    val reaction = doc.getDouble("reactionScore") ?: 0.0

                    overallEntries.add(Entry(index, overall.toFloat()))
                    memoryEntries.add(Entry(index, memory.toFloat()))
                    reactionEntries.add(Entry(index, reaction.toFloat()))

                    index++
                }

                setupChart(chartOverall, overallEntries, "Overall Score")
                setupChart(chartMemory, memoryEntries, "Memory Score")
                setupChart(chartReaction, reactionEntries, "Reaction Score")
            }
    }

    private fun setupChart(chart: LineChart, entries: List<Entry>, label: String) {

        val dataSet = LineDataSet(entries, label)

        val lineData = LineData(dataSet)

        chart.data = lineData
        chart.invalidate()
    }
}