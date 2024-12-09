package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R

class SEEMarksheetFragment : Fragment() {

    private lateinit var marksRecyclerView: RecyclerView
    private lateinit var marksAdapter: SEEMarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_see_marksheet, container, false)

        // Initialize RecyclerView
        marksRecyclerView = view.findViewById(R.id.seeMarksRecyclerView)
        marksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Pass the dataset to the adapter
        val marksList = getMarksList()
        marksAdapter = SEEMarksAdapter(marksList)
        marksRecyclerView.adapter = marksAdapter

        return view
    }

    // Function to get the marks data
    private fun getMarksList(): List<SEEMarks> {
        // Replace this with real data fetching logic
        return listOf(
            SEEMarks(202158, 12, 11, 12, 12, 11, 58),
            SEEMarks(202159, 11, 10, 10, 10, 10, 51),
            SEEMarks(202160, 10, 10, 9, 9, 9, 47),
            // Add more mock data here...
        )
    }
}