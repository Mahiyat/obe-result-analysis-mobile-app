package com.example.obe_result_analysis_mobile_app_1.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R
import com.example.obe_result_analysis_mobile_app_1.network.CountsResponse
import com.example.obe_result_analysis_mobile_app_1.network.PendingActivity
import com.example.obe_result_analysis_mobile_app_1.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var activityAdapter: ActivityAdapter
    private var pendingActivities: MutableList<PendingActivity> = mutableListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var marksRemainingText: TextView
    private lateinit var marksSubmissionRemainingText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView and TextViews
        recyclerView = root.findViewById(R.id.recyclerViewPendingActivities)
        marksRemainingText = root.findViewById(R.id.marksRemainingText) // Ensure this ID matches your layout
        marksSubmissionRemainingText = root.findViewById(R.id.marksSubmissionRemainingText) // Ensure this ID matches your layout

//        // Setup RecyclerView with an empty adapter
//        setupRecyclerView()
//
//        // Fetch data
//        fetchCounts()
//        fetchPendingActivities()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated called")

        // Setup RecyclerView
        setupRecyclerView()
        Log.d("HomeFragment", "setupRecyclerView called in onViewCreated")

        // Fetch data
        fetchCounts()
        fetchPendingActivities()
    }

    private fun setupRecyclerView() {
        Log.d("Home Fragment", "Recycler Setup Called")
        activityAdapter = ActivityAdapter(pendingActivities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = activityAdapter
    }

    private fun fetchCounts() {
        RetrofitClient.instance.getCounts().enqueue(object : Callback<CountsResponse> {
            override fun onResponse(call: Call<CountsResponse>, response: Response<CountsResponse>) {
                if (response.isSuccessful) {
                    val counts = response.body()
                    marksRemainingText.text = counts?.all_courses_count.toString()
                    marksSubmissionRemainingText.text = counts?.pending_courses_count.toString()
                    Log.d("HomeFragment", "Counts: Total Courses - ${counts?.all_courses_count}, Marks Submission Remaining - ${counts?.pending_courses_count}")
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch counts", Toast.LENGTH_SHORT).show()
                    Log.e("HomeFragment", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CountsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "API call failure: ${t.message}")
            }
        })
    }

    private fun fetchPendingActivities() {
        RetrofitClient.instance.getPendingActivities().enqueue(object : Callback<List<PendingActivity>> {
            override fun onResponse(call: Call<List<PendingActivity>>, response: Response<List<PendingActivity>>) {
                if (response.isSuccessful) {
                    val fetchedActivities = response.body() ?: emptyList()
                    pendingActivities.clear() // Clear the existing list
                    pendingActivities.addAll(fetchedActivities) // Add new activities
                    Log.d("HomeFragment", "Activities Fetched: $pendingActivities")

                    // Notify the adapter that the data has changed
                    activityAdapter.updateActivities(fetchedActivities)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch pending activities", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PendingActivity>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}