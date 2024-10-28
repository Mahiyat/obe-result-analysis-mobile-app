package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class CIEMarksAdapter(private val marksList: List<CIEMarks>) : RecyclerView.Adapter<CIEMarksAdapter.MarksViewHolder>() {

    class MarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rollTextView: TextView = itemView.findViewById(R.id.cieRollTextView)
        val tutorialTextView: TextView = itemView.findViewById(R.id.tutorialTextView)
        val assignmentTextView: TextView = itemView.findViewById(R.id.assignmentTextView)
        val curricularTextView: TextView = itemView.findViewById(R.id.curricularTextView)
        val quizTextView: TextView = itemView.findViewById(R.id.quizTextView)
        val totalTextView: TextView = itemView.findViewById(R.id.totalTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cie_marks_item, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        val marks = marksList[position]
        holder.rollTextView.text = marks.roll.toString()
        holder.tutorialTextView.text = marks.tutorial.toString()
        holder.assignmentTextView.text = marks.assignment.toString()
        holder.curricularTextView.text = marks.curricular.toString()
        holder.quizTextView.text = marks.quiz.toString()
        holder.totalTextView.text = marks.total.toString()
    }

    override fun getItemCount(): Int = marksList.size
}