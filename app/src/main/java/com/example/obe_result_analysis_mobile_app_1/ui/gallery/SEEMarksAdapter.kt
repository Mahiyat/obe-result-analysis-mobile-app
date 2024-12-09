package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class SEEMarksAdapter(private val marksList: List<SEEMarks>) : RecyclerView.Adapter<SEEMarksAdapter.MarksViewHolder>() {

    class MarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examRollTextView: TextView = itemView.findViewById(R.id.seeExamRollTextView)
        val clo1TextView: TextView = itemView.findViewById(R.id.clo1TextView)
        val clo2TextView: TextView = itemView.findViewById(R.id.clo2TextView)
        val clo3TextView: TextView = itemView.findViewById(R.id.clo3TextView)
        val clo4TextView: TextView = itemView.findViewById(R.id.clo4TextView)
        val clo5TextView: TextView = itemView.findViewById(R.id.clo5TextView)
        val totalTextView: TextView = itemView.findViewById(R.id.totalTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_see_marks_item, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        val marks = marksList[position]
        holder.examRollTextView.text = marks.examRoll.toString()
        holder.clo1TextView.text = marks.clo1.toString()
        holder.clo2TextView.text = marks.clo2.toString()
        holder.clo3TextView.text = marks.clo3.toString()
        holder.clo4TextView.text = marks.clo4.toString()
        holder.clo5TextView.text = marks.clo5.toString()
        holder.totalTextView.text = marks.total.toString()
    }

    override fun getItemCount(): Int = marksList.size
}