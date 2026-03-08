package com.example.neura

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.neura.ProfileActivity
class HistoryActivity : AppCompatActivity() {
    private lateinit var chart: LineChart
    private lateinit var recyclerView: RecyclerView
    private val sessionList = mutableListOf<TestSession>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        chart = findViewById(R.id.scoreChart)
        recyclerView = findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_history
        bottomNav.setOnItemSelectedListener { item ->

            when(item.itemId){

                R.id.nav_home -> {
                    startActivity(Intent(this,MainActivity::class.java))
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
            }
        }
        loadSessions()
    }

    private fun loadSessions() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("sessions")
            .orderBy("timestamp",
                com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                sessionList.clear()

                for (doc in result) {
                    val session = doc.toObject(TestSession::class.java)
                    sessionList.add(session)
                }

                val adapter = SessionAdapter(sessionList) { session ->

                    val intent = Intent(this, SessionDetailActivity::class.java)

                    intent.putExtra("memory", session.memoryScore)
                    intent.putExtra("reaction", session.reactionScore)
                    intent.putExtra("speech", session.speechScore)
                    intent.putExtra("drawing", session.drawingScore)
                    intent.putExtra("orientation", session.orientationScore)
                    intent.putExtra("final", session.finalScore)
                    intent.putExtra("risk", session.riskLevel)

                    startActivity(intent)
                }

                recyclerView.adapter = adapter
                updateChart()
            }
    }
    private fun updateChart() {

        val entries = ArrayList<Entry>()

        for (i in sessionList.indices) {
            val score = sessionList[i].finalScore
            entries.add(Entry(i.toFloat(), score))
        }

        val dataSet = LineDataSet(entries, "Cognitive Score Progress")

        dataSet.lineWidth = 3f
        dataSet.circleRadius = 5f

        val lineData = LineData(dataSet)

        chart.data = lineData
        chart.description.text = "Score Trend"
        chart.invalidate()
    }
}