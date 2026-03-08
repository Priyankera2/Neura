package com.example.neura

import kotlin.math.*

object DrawingAnalyzer {

    fun computeDrawingScore(points: List<StrokePoint>): Float {

        if(points.size < 50) return 10f

        val center = getClockCenter(points)
        val circleScore = circleRoundness(points, center)
        val tremorScore = tremor(points)
        val handScore = clockHandAccuracy(points, center)

        var score = 0f

        score += circleScore * 40
        score += (1 - tremorScore) * 30
        score += handScore * 30

        return score.coerceIn(0f,100f)
    }

    private fun getClockCenter(points: List<StrokePoint>): Pair<Float,Float>{

        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }

        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        return Pair((minX+maxX)/2,(minY+maxY)/2)
    }

    private fun circleRoundness(
        points: List<StrokePoint>,
        center: Pair<Float,Float>
    ): Float{

        val cx = center.first
        val cy = center.second

        val radii = points.map{
            sqrt((it.x-cx).pow(2)+(it.y-cy).pow(2))
        }

        val avg = radii.average()

        val variance = radii.sumOf{
            (it-avg).pow(2)
        } / radii.size

        val deviation = sqrt(variance)

        return (1 - (deviation / avg)).toFloat().coerceIn(0f,1f)
    }

    private fun tremor(points: List<StrokePoint>): Float{

        var total = 0f

        for(i in 2 until points.size){

            val dx1 = points[i-1].x - points[i-2].x
            val dy1 = points[i-1].y - points[i-2].y

            val dx2 = points[i].x - points[i-1].x
            val dy2 = points[i].y - points[i-1].y

            val angle1 = atan2(dy1,dx1)
            val angle2 = atan2(dy2,dx2)

            total += abs(angle2-angle1)
        }

        val avg = total/points.size

        return (avg/PI).toFloat().coerceIn(0f,1f)
    }

    private fun clockHandAccuracy(
        points: List<StrokePoint>,
        center: Pair<Float,Float>
    ): Float{

        val detected = detectHandAngles(points,center)

        val minuteAngle = ClockTaskGenerator.minute * 6f
        val hourAngle =
            (ClockTaskGenerator.hour % 12)*30f +
                    ClockTaskGenerator.minute*0.5f

        val err1 = angleError(detected.first,minuteAngle)
        val err2 = angleError(detected.second,hourAngle)

        val score = 1f - ((err1+err2)/360f)

        return score.coerceIn(0f,1f)
    }

    private fun detectHandAngles(
        points: List<StrokePoint>,
        center: Pair<Float,Float>
    ): Pair<Float,Float>{

        val cx = center.first
        val cy = center.second

        val farPoints = points.sortedByDescending{
            (it.x-cx).pow(2)+(it.y-cy).pow(2)
        }.take(30)

        val angles = farPoints.map{

            val dx = it.x - cx
            val dy = cy - it.y

            Math.toDegrees(atan2(dy,dx).toDouble()).toFloat()
        }

        return Pair(angles[0],angles[1])
    }

    private fun angleError(a:Float,b:Float):Float{

        var diff = abs(a-b)

        if(diff>180) diff = 360-diff

        return diff
    }
}