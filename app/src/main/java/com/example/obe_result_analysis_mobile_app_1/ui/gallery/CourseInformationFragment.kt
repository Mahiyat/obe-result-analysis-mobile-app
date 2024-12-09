package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class CourseInformationFragment : Fragment() {

    private lateinit var courseId: String
    private lateinit var courseName: String
    private lateinit var examRecyclerView: RecyclerView
    private lateinit var examsAdapter: CIEAdapter
    private lateinit var btnViewMarksheet: Button
    private lateinit var btnSubmitMarks: Button
    private lateinit var seeMarksDetails: TextView

    private val examsList = listOf("Tutorial-1", "Assignment", "Quiz", "Tutorial-2", "Curricular")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_course_information, container, false)

        // Retrieve arguments from CourseAdapter
        arguments?.let {
            courseId = it.getString("courseId").orEmpty()
            courseName = it.getString("courseName").orEmpty()
        }

        val courseIdTextView: TextView = view.findViewById(R.id.text_course_id)
        val courseNameTextView: TextView = view.findViewById(R.id.text_course_name)
        examRecyclerView = view.findViewById(R.id.recycler_view_exams)
        btnViewMarksheet = view.findViewById(R.id.btn_view_marksheet)
        btnSubmitMarks = view.findViewById(R.id.btn_submit_marks)
        seeMarksDetails = view.findViewById(R.id.tv_semester_end_exam_details)

        // Set course information in the views
        courseIdTextView.text = "Course ID: $courseId"
        courseNameTextView.text = "Course Name: $courseName"

        examRecyclerView = view.findViewById(R.id.recycler_view_exams)
        examRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        examsAdapter = CIEAdapter(examsList)
        examRecyclerView.adapter = examsAdapter

        // Handle button clicks
        btnViewMarksheet.setOnClickListener {
            // Handle View Marksheet button click
            it.findNavController().navigate(R.id.action_courseInformationFragment_to_cieMarksheetFragment)

        }

        btnSubmitMarks.setOnClickListener {
            // Handle Submit Marks button click
            Toast.makeText(requireContext(), "Marks successfully submitted!", Toast.LENGTH_SHORT).show()
        }

        seeMarksDetails.setOnClickListener {

            it.findNavController().navigate(R.id.action_courseInformationFragment_to_seeMarksheetFragment)
        }

        return view
    }
}
