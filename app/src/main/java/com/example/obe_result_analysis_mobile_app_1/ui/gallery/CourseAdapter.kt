package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class CourseAdapter(private val courseList: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseId: TextView = itemView.findViewById(R.id.course_id)
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val viewIcon: ImageView = itemView.findViewById(R.id.view_course)
        val editIcon: ImageView = itemView.findViewById(R.id.edit_course)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_course)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.courseId.text = course.courseId
        holder.courseName.text = course.courseName

        // Set actions for icons
        holder.viewIcon.setOnClickListener {
            // Handle view action
            val bundle = bundleOf(
                "courseId" to course.courseId,
                "courseName" to course.courseName
            )
            it.findNavController().navigate(R.id.action_courseListFragment_to_courseInformationFragment, bundle)
        }

        holder.editIcon.setOnClickListener {
            // Handle edit action
        }

        holder.deleteIcon.setOnClickListener {
            // Handle delete action
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}
