package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.obe_result_analysis_mobile_app_1.R

class CourseDetailsFragment : Fragment() {

    private lateinit var courseId: String
    private lateinit var courseName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_course_information, container, false)

        // Retrieve the arguments passed from CourseInformationFragment
        arguments?.let {
            courseId = it.getString("courseId").orEmpty()
            courseName = it.getString("courseName").orEmpty()
        }

        // Find views and set course details
        val courseIdTextView: TextView = view.findViewById(R.id.text_course_id)
        val courseNameTextView: TextView = view.findViewById(R.id.text_course_name)

        courseIdTextView.text = "Course ID: $courseId"
        courseNameTextView.text = "Course Name: $courseName"

        return view
    }

    companion object {
        fun newInstance(courseId: String, courseName: String) =
            CourseDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("courseId", courseId)
                    putString("courseName", courseName)
                }
            }
    }
}
