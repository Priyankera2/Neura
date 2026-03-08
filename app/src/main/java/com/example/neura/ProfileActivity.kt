package com.example.neura

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etGender: EditText
    private lateinit var etEducation: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etGender = findViewById(R.id.etGender)
        etEducation = findViewById(R.id.etEducation)

        btnSave = findViewById(R.id.btnSaveProfile)
        btnLogout = findViewById(R.id.btnLogout)

        loadProfile()

        btnSave.setOnClickListener {
            saveProfile()
        }

        btnLogout.setOnClickListener {
            logout()
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_profile
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

    private fun saveProfile() {

        val uid = auth.currentUser?.uid ?: return

        val data = hashMapOf(
            "name" to etName.text.toString(),
            "age" to etAge.text.toString().toIntOrNull(),
            "gender" to etGender.text.toString(),
            "education" to etEducation.text.toString(),
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(uid)
            .set(data)
            .addOnSuccessListener {
                finish()
            }
    }

    private fun loadProfile() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {

                    etName.setText(doc.getString("name"))
                    etAge.setText(doc.getLong("age")?.toString())
                    etGender.setText(doc.getString("gender"))
                    etEducation.setText(doc.getString("education"))

                }
            }
    }

    private fun logout() {

        auth.signOut()

        startActivity(
            Intent(this, LoginActivity::class.java)
        )

        finish()
    }
}