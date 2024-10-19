package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class CIEIndividualMarksAdapter(private val marksList: List<CIEIndividualMarks>) : RecyclerView.Adapter<CIEIndividualMarksAdapter.MarksViewHolder>() {

    class MarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rollTextView: TextView = itemView.findViewById(R.id.rollTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val marksTextView: TextView = itemView.findViewById(R.id.marksTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_individual_cie_marks_item, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        val marks = marksList[position]
        holder.rollTextView.text = marks.roll.toString()
        holder.nameTextView.text = marks.name
        holder.marksTextView.text = marks.marks.toString()
    }

    override fun getItemCount(): Int = marksList.size
}
