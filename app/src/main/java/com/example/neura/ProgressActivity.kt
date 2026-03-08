package com.example.neura

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProgressActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_progress)

        chart = findViewById(R.id.progressChart)


        loadProgress()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->

            when(item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.nav_history -> {

                    startActivity(
                        Intent(this, HistoryActivity::class.java)
                    )

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(this, ProfileActivity::class.java)
                    )

                    true
                }

                else -> false
            }}
    }

    private fun loadProgress() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("sessions")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->

                val memoryEntries = ArrayList<Entry>()
                val reactionEntries = ArrayList<Entry>()
                val speechEntries = ArrayList<Entry>()
                val drawingEntries = ArrayList<Entry>()
                val orientationEntries = ArrayList<Entry>()

                var index = 0f

                for (doc in result) {

                    val memory = doc.getDouble("memoryScore") ?: 0.0
                    val reaction = doc.getDouble("reactionScore") ?: 0.0
                    val speech = doc.getDouble("speechScore") ?: 0.0
                    val drawing = doc.getDouble("drawingScore") ?: 0.0
                    val orientation = doc.getDouble("orientationScore") ?: 0.0

                    memoryEntries.add(Entry(index, memory.toFloat()))
                    reactionEntries.add(Entry(index, reaction.toFloat()))
                    speechEntries.add(Entry(index, speech.toFloat()))
                    drawingEntries.add(Entry(index, drawing.toFloat()))
                    orientationEntries.add(Entry(index, orientation.toFloat()))

                    index++
                }

                val memorySet = LineDataSet(memoryEntries, "Memory")
                val reactionSet = LineDataSet(reactionEntries, "Reaction")
                val speechSet = LineDataSet(speechEntries, "Speech")
                val drawingSet = LineDataSet(drawingEntries, "Drawing")
                val orientationSet = LineDataSet(orientationEntries, "Orientation")

                memorySet.color = Color.BLUE
                reactionSet.color = Color.RED
                speechSet.color = Color.GREEN
                drawingSet.color = Color.MAGENTA
                orientationSet.color = Color.rgb(255,165,0)

                val data = LineData(
                    memorySet,
                    reactionSet,
                    speechSet,
                    drawingSet,
                    orientationSet
                )

                chart.data = data
                chart.description.isEnabled = false
                chart.invalidate()
            }

    }

}