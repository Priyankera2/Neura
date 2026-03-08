package com.example.neura

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

class DrawingTest : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var btnClear: Button
    private lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_test)

        drawingView = findViewById(R.id.drawingView)
        btnClear = findViewById(R.id.btn_clear)
        btnConfirm = findViewById(R.id.btn_confirm)

        btnClear.setOnClickListener {
            drawingView.clearCanvas()
        }

        btnConfirm.setOnClickListener {

            if(drawingView.strokePoints.size < 30){
                Toast.makeText(this,"Draw the clock first",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val score = DrawingAnalyzer
                .computeDrawingScore(drawingView.strokePoints)

            TestResultStore.drawingScore = score

            AlertDialog.Builder(this)
                .setTitle("Drawing Test Complete")
                .setMessage("Drawing Score: %.1f".format(score))
                .setPositiveButton("Continue"){_,_->

                    startActivity(
                        Intent(this, OrientationTest::class.java)
                    )
                    finish()
                }
                .show()
        }

        showInstruction()
    }

    // Instruction dialog
    private fun showInstruction() {

        ClockTaskGenerator.generate()

        AlertDialog.Builder(this)
            .setTitle("Clock Drawing Test")
            .setMessage(
                "Draw a clock showing time ${ClockTaskGenerator.getTimeString()}\n\n" +
                        "Include:\n" +
                        "• Circle\n" +
                        "• Numbers\n" +
                        "• Two hands"
            )
            .setCancelable(false)
            .setPositiveButton("Start", null)
            .show()
    }
    // Save drawing image locally
    private fun saveDrawing() {

        try {

            val bitmap: Bitmap = drawingView.getBitmap()

            val file = File(filesDir, "clock_test.png")

            val stream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            stream.flush()
            stream.close()

            Log.d("DrawingTest", "Drawing saved: ${file.absolutePath}")

        } catch (e: Exception) {

            Log.e("DrawingTest", "Saving error: ${e.message}")
        }
    }

    // Result dialog

}