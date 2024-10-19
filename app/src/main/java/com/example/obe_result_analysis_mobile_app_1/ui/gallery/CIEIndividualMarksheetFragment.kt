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

class CIEIndividualMarksheetFragment : Fragment() {

    private lateinit var marksRecyclerView: RecyclerView
    private lateinit var marksAdapter: CIEIndividualMarksAdapter
    private lateinit var examName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_individual_cie_marksheet, container, false)

        arguments?.let {
            examName = it.getString("examName").orEmpty()
            if (examName.isEmpty()) {
                Toast.makeText(requireContext(), "Exam name not passed!", Toast.LENGTH_SHORT).show()
            }
        }


        val examNameTextView: TextView = view.findViewById(R.id.cie_exam_title)
        examNameTextView.text = examName

        // Initialize RecyclerView
        marksRecyclerView = view.findViewById(R.id.marksRecyclerView)
        marksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Pass the dataset to the adapter
        val marksList = getMarksList()
        marksAdapter = CIEIndividualMarksAdapter(marksList)
        marksRecyclerView.adapter = marksAdapter

        return view
    }

    // Function to get the marks data
    private fun getMarksList(): List<CIEIndividualMarks> {
        // Replace this with real data fetching logic
        return listOf(
            CIEIndividualMarks(346, "Solaimi Hamid", 10),
            CIEIndividualMarks(347, "Sadia Hossain", 17),
            CIEIndividualMarks(348, "Samia Alam", 20),
            // Add more mock data here...
        )
    }
}
