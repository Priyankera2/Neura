package com.example.neura

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
data class StrokePoint(
    val x: Float,
    val y: Float,
    val time: Long
)
class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint()

    private val paths = mutableListOf<Path>()
    val strokePoints = mutableListOf<StrokePoint>()

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 12f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (p in paths) {
            canvas.drawPath(p, paint)
        }

        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                strokePoints.add(StrokePoint(x, y, System.currentTimeMillis()))
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                strokePoints.add(StrokePoint(x, y, System.currentTimeMillis()))
            }

            MotionEvent.ACTION_UP -> {
                paths.add(Path(path))
                strokePoints.add(StrokePoint(x, y, System.currentTimeMillis()))
                path.reset()
            }
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        paths.clear()
        path.reset()
        strokePoints.clear()
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }
}