// ResultStatisticsFragment.kt
package com.example.obe_result_analysis_mobile_app_1.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.obe_result_analysis_mobile_app_1.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class ResultStatisticsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var courseSpinner: Spinner

    inner class CustomXAxisValueFormatter : ValueFormatter() {
        private val labels = arrayOf("CLO1", "CLO2", "CLO3", "CLO4", "CLO5")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return labels.getOrNull(value.toInt()) ?: ""
        }

        override fun getBarLabel(barEntry: BarEntry?): String {
            return labels.getOrNull(barEntry?.x?.toInt() ?: -1) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_statistics, container, false)

        barChart = view.findViewById(R.id.bar_chart)
        courseSpinner = view.findViewById(R.id.course_spinner)

        setupSpinner()
        setupBarChart()

        return view
    }

    private fun setupSpinner() {
        // Sample course data
        val courses = listOf("CSE-203", "CSE-357", "CSE-353")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        courseSpinner.adapter = adapter
    }

    private fun setupBarChart() {
        // Dummy data for CLO marks
        val entries = listOf(
            BarEntry(0f, 85f), // CLO1
            BarEntry(1f, 90f), // CLO2
            BarEntry(2f, 70f), // CLO3
            BarEntry(3f, 75f), // CLO4
            BarEntry(4f, 80f)  // CLO5
        )

        val dataSet = BarDataSet(entries, "CLO Marks")

        val barData = BarData(dataSet)


        barChart.data = barData
        barChart.description.text=""
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = CustomXAxisValueFormatter() // Use the custom formatter
        xAxis.granularity = 1f
        barChart.invalidate() // Refresh the chart
    }
}
