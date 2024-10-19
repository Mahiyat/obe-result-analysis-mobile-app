package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

data class Exam(val name: String, val details: String)

class CIEAdapter(private val exams: List<String>) : RecyclerView.Adapter<CIEAdapter.ExamsViewHolder>() {

    class ExamsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val examName: TextView = view.findViewById(R.id.exam_name)
        val viewDetailsButton: ImageButton = view.findViewById(R.id.btn_view_exam_details)
        val editButton: ImageButton = view.findViewById(R.id.btn_edit_exam)
        val deleteButton: ImageButton = view.findViewById(R.id.btn_delete_exam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cie, parent, false)
        return ExamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamsViewHolder, position: Int) {
        val exam = exams[position]
        holder.examName.text = exam

        // Set onClick listeners for each button if necessary
        holder.viewDetailsButton.setOnClickListener {
            val bundle = bundleOf(
                "examName" to exam
            )
            it.findNavController().navigate(R.id.action_courseInformationFragment_to_individualCIEMarksheetFragment, bundle)
        }
        holder.editButton.setOnClickListener {
            // Handle edit exam
        }
        holder.deleteButton.setOnClickListener {
            // Handle delete exam
        }
    }

    override fun getItemCount() = exams.size
}
