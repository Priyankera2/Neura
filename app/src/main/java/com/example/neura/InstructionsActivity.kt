package com.example.neura

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView


class InstructionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)

        val startBtn = findViewById<Button>(R.id.btnStartTest)

        startBtn.setOnClickListener {

            startActivity(
                Intent(this, MemoryTest::class.java)
            )

        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
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