package com.example.neura

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var start: MaterialCardView
    private lateinit var instruction: MaterialCardView
    private lateinit var progress: MaterialCardView
    private lateinit var history: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        start=findViewById<MaterialCardView>(R.id.cardTest)
        instruction=findViewById<MaterialCardView>(R.id.cardInstruction)
        progress=findViewById<MaterialCardView>(R.id.cardProgress)
        history=findViewById<MaterialCardView>(R.id.cardHistory)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {

                val name = it.getString("name") ?: "User"

                findViewById<TextView>(R.id.tvGreeting).text = "Hello, $name!"
            }
        start.setOnClickListener {
            startActivity(Intent(this, MemoryTest::class.java))
        }
        instruction.setOnClickListener {
            startActivity(Intent(this, InstructionsActivity::class.java))
        }
        progress.setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }
        history.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home
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

    }
}