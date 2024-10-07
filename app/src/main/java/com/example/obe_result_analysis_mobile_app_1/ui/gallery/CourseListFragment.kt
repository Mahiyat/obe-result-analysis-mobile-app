package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CourseListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_course_list, container, false)

        // Sample data for courses
        val pendingCourses = listOf(
            Course("CSE-209", "Algorithm-I", "2nd Year 1st Semester 2023"),
            Course("CSE-255", "Database Systems", "2nd Year 2nd Semester 2024")
        )

        val completedCourses = listOf(
            Course("CSE-203", "Computer Ethics and Cyber Law", "2nd Year 1st Semester 2023"),
            Course("CSE-357", "Microprocessors", "3rd Year 2nd Semester 2024"),
            Course("CSE-353", "Human Computer Interaction", "3rd Year 2nd Semester 2022")
        )

        // Setup Pending Courses RecyclerView
        val pendingRecyclerView = view.findViewById<RecyclerView>(R.id.pending_courses_recycler_view)
        pendingRecyclerView.layoutManager = LinearLayoutManager(context)
        pendingRecyclerView.adapter = CourseAdapter(pendingCourses)

        // Setup Completed Courses RecyclerView
        val completedRecyclerView = view.findViewById<RecyclerView>(R.id.completed_courses_recycler_view)
        completedRecyclerView.layoutManager = LinearLayoutManager(context)
        completedRecyclerView.adapter = CourseAdapter(completedCourses)

        return view
    }
}

