package com.example.neura

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Color
class SessionAdapter(
    private val sessions: List<TestSession>,
    private val onClick: (TestSession) -> Unit
) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val tvRisk: TextView = view.findViewById(R.id.tvRisk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.rotation = (-2..2).random().toFloat()
        val session = sessions[position]

        val date = Date(session.timestamp)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        holder.tvDate.text = formatter.format(date)
        holder.tvScore.text = "Score: ${session.finalScore.toInt()}"
        holder.tvRisk.text = "Risk: ${session.riskLevel}"
        when (session.riskLevel) {
            "Low Risk" -> holder.tvRisk.setTextColor(Color.parseColor("#2E7D32"))   // Green
            "Moderate Risk" -> holder.tvRisk.setTextColor(Color.parseColor("#F9A825")) // Yellow/Orange
            "High Risk" -> holder.tvRisk.setTextColor(Color.parseColor("#C62828"))  // Red
            else -> holder.tvRisk.setTextColor(Color.GRAY)
        }

        holder.itemView.setOnClickListener {
            onClick(session)
        }
    }

    override fun getItemCount() = sessions.size
}