package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class CIEMarksheetFragment : Fragment() {

    private lateinit var marksRecyclerView: RecyclerView
    private lateinit var marksAdapter: CIEMarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cie_marksheet, container, false)

        // Initialize RecyclerView
        marksRecyclerView = view.findViewById(R.id.cieMarksRecyclerView)
        marksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Pass the dataset to the adapter
        val marksList = getMarksList()
        marksAdapter = CIEMarksAdapter(marksList)
        marksRecyclerView.adapter = marksAdapter

        return view
    }

    // Function to get the marks data
    private fun getMarksList(): List<CIEMarks> {
        // Replace this with real data fetching logic
        return listOf(
            CIEMarks(346, 10, 10, 5, 5, 30),
            CIEMarks(347, 17, 10, 4, 4, 35),
            CIEMarks(348, 15, 10, 4, 4, 33),
            // Add more mock data here...
        )
    }
}