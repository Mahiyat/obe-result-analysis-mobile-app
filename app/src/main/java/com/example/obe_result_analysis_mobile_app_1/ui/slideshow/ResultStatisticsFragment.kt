package com.example.obe_result_analysis_mobile_app_1.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.obe_result_analysis_mobile_app_1.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight

class ResultStatisticsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var stackedBarChart: BarChart
    private lateinit var courseSpinner: Spinner
    private lateinit var examTitleSpinner: Spinner
    private lateinit var incourseTypeSpinner: Spinner
    private lateinit var evaluationTypeSpinner: Spinner
    private lateinit var seeSpinner: Spinner
    private lateinit var btnShowComments: Button
    private lateinit var toggleChart: Switch // Toggle for switching charts

    private var courseSelected = false
    private var examTitleSelected = false
    private var incourseTypeSelected = false
    private var isSingleCourseMode = false // Track whether it's Single Course mode


    inner class CustomXAxisValueFormatter : ValueFormatter() {
        private val labels = arrayOf("CLO1", "CLO2", "CLO3", "CLO4", "CLO5")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return labels.getOrNull(value.toInt()) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_statistics, container, false)

        // Cast to SwipeRefreshLayout explicitly
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        // Set up the swipe refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            refreshPage() // Call the refresh method when swipe is detected
            swipeRefreshLayout.isRefreshing = false // Stop the refreshing indicator
        }

        barChart = view.findViewById(R.id.bar_chart)
        pieChart = view.findViewById(R.id.pie_chart)
        stackedBarChart = view.findViewById(R.id.stacked_bar_chart)
        courseSpinner = view.findViewById(R.id.course_spinner)
        examTitleSpinner = view.findViewById(R.id.exam_title_spinner)
        incourseTypeSpinner = view.findViewById(R.id.incourse_type_spinner)
        evaluationTypeSpinner = view.findViewById(R.id.evaluation_type_spinner)
        seeSpinner = view.findViewById(R.id.see_spinner)
        btnShowComments = view.findViewById(R.id.btn_show_comments)
        toggleChart = view.findViewById(R.id.toggle_chart) // Initialize the toggle

        setupEvaluationSpinner()
        setupCharts()

        return view
    }

    private fun refreshPage() {
        // Reset all spinners to the first item (default)
        evaluationTypeSpinner.setSelection(0)
        seeSpinner.setSelection(0)
        courseSpinner.setSelection(0)
        examTitleSpinner.setSelection(0)
        incourseTypeSpinner.setSelection(0)

        seeSpinner.visibility = View.GONE
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        incourseTypeSpinner.visibility = View.GONE

        // Hide all charts
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        stackedBarChart.visibility = View.GONE

        // Hide buttons and toggle
        btnShowComments.visibility = View.GONE
        toggleChart.visibility = View.GONE

        // Reset selection flags
        courseSelected = false
        examTitleSelected = false
        incourseTypeSelected = false

        // Optionally, reset the description of the charts or any other UI elements
        barChart.clear()
        pieChart.clear()
        stackedBarChart.clear()
    }

    private fun setupEvaluationSpinner() {
        val evaluationTypes = listOf("Select Evaluation Type", "Continuous Internal Evaluation", "Semester End Examination")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, evaluationTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        evaluationTypeSpinner.adapter = adapter

        evaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (evaluationTypes[position]) {
                    "Continuous Internal Evaluation" -> if (position != 0) showCIEOptions()
                    "Semester End Examination" -> if (position != 0) showSEEOptions()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showCIEOptions() {
        courseSpinner.setSelection(0)
        examTitleSpinner.setSelection(0)
        incourseTypeSpinner.setSelection(0)
        val courses = listOf("Select a Course", "CSE-203", "CSE-357", "CSE-353")
        val exams = listOf("Select Exam Title", "2nd Year 1st Semester 2020", "3rd Year 2nd Semester 2020", "3rd Year 2nd Semester 2021")
        val incourseTypes = listOf("Select Incourse Type", "Tutorial", "Assignment", "Quiz", "Curricular/Co-curricular Activities")

        courseSpinner.visibility = View.VISIBLE
        examTitleSpinner.visibility = View.VISIBLE
        incourseTypeSpinner.visibility = View.GONE // Initially hidden
        seeSpinner.visibility = View.GONE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        btnShowComments.visibility = View.GONE
        toggleChart.visibility = View.GONE // Hide toggle initially

        courseSelected = false
        examTitleSelected = false
        incourseTypeSelected = false

        setupSpinner(courseSpinner, courses) { position ->
            courseSelected = position != 0
            checkCIESelections(exams, incourseTypes)
        }

        setupSpinner(examTitleSpinner, exams) { position ->
            examTitleSelected = position != 0
            checkCIESelections(exams, incourseTypes)
        }

        setupSpinner(incourseTypeSpinner, incourseTypes) { position ->
            incourseTypeSelected = position != 0
            if (incourseTypeSelected) {
                renderPieChart(incourseTypes[position]) // Pass incourse type to set description
                toggleChart.visibility = View.VISIBLE // Show toggle when pie chart is rendered
            }
        }
    }

    private fun checkCIESelections(exams: List<String>, incourseTypes: List<String>) {
        if (courseSelected && examTitleSelected) {
            incourseTypeSpinner.visibility = View.VISIBLE // Show incourse type spinner when both course and exam are selected
        } else {
            incourseTypeSpinner.visibility = View.GONE
        }
    }

//    private fun showSEEOptions() {
//        val coursesSEE = listOf("Select SEE Course", "CSE-203", "CSE-357", "CSE-353")
//
//        seeSpinner.visibility = View.VISIBLE
//        courseSpinner.visibility = View.GONE
//        examTitleSpinner.visibility = View.GONE
//        incourseTypeSpinner.visibility = View.GONE
//        barChart.visibility = View.GONE
//        pieChart.visibility = View.GONE
//        btnShowComments.visibility = View.GONE
//        toggleChart.visibility = View.GONE // Hide toggle for SEE options
//
//        setupSpinner(seeSpinner, coursesSEE) { position ->
//            if (position != 0) {
//                renderBarChart() // Render the bar chart after SEE course is selected
//            }
//        }
//    }

    private fun showSEEOptions() {
        val seeOptions = listOf("Select SEE Type", "Single Course", "All Courses")

        // Populate SEE type spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seeSpinner.adapter = adapter

        // Show the SEE options spinner
        seeSpinner.visibility = View.VISIBLE
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        incourseTypeSpinner.visibility = View.GONE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        btnShowComments.visibility = View.GONE
        toggleChart.visibility = View.GONE // Hide toggle initially

        seeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (seeOptions[position]) {
                    "Single Course" -> showSingleCourseSEEOptions()
                    "All Courses" -> renderStackedBarChartForAllCourses()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSpinner(spinner: Spinner, options: List<String>, onSelectAction: (Int) -> Unit) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Ignore the first item (placeholder)
                if (position == 0) return
                onSelectAction(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCharts() {
        // Setup initial chart display (empty)
    }

//    private fun renderBarChart() {
//        val entries = listOf(
//            BarEntry(0f, 85f), // CLO1
//            BarEntry(1f, 90f), // CLO2
//            BarEntry(2f, 70f), // CLO3
//            BarEntry(3f, 75f), // CLO4
//            BarEntry(4f, 80f)  // CLO5
//        )
//        val dataSet = BarDataSet(entries, "CLO Marks")
//        dataSet.colors = listOf(
//            ContextCompat.getColor(requireContext(), R.color.color2)
//        )
//        val barData = BarData(dataSet)
//        barChart.data = barData
//        barChart.xAxis.valueFormatter = CustomXAxisValueFormatter()
//        barChart.xAxis.granularity = 1f
//        barChart.description.text = ""
//        barChart.axisLeft.setDrawGridLines(false)
//        barChart.axisRight.isEnabled = false
//        barChart.xAxis.setDrawGridLines(false)
//        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        barChart.invalidate() // Refresh chart
//
//        barChart.visibility = View.VISIBLE // Make chart visible
//        btnShowComments.visibility = View.VISIBLE // Show "Show Comments" button
//        toggleChart.visibility = View.GONE // Hide toggle when showing bar chart
//    }

    private fun showSingleCourseSEEOptions() {
        courseSpinner.setSelection(0)
        examTitleSpinner.setSelection(0)
        isSingleCourseMode = true // Set Single Course mode
        val coursesSEE = listOf("Select SEE Course", "CSE-203", "CSE-357", "CSE-353")
        val examsSEE = listOf("Select Exam Title", "2nd Year 2nd Semester 2020", "3rd Year 2nd Semester 2020")

        courseSpinner.visibility = View.VISIBLE
        examTitleSpinner.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        btnShowComments.visibility = View.GONE

        // Set up spinners for course and exam title
        setupSpinner(courseSpinner, coursesSEE) { coursePosition ->
            setupSpinner(examTitleSpinner, examsSEE) { examPosition ->
                if (coursePosition != 0 && examPosition != 0) {
                    renderBarChartForSingleCourse() // Render the bar chart for the selected course and exam
                }
            }
        }
    }

    private fun renderBarChartForSingleCourse() {
        val entries = listOf(
            BarEntry(0f, 85f), // CLO1
            BarEntry(1f, 90f), // CLO2
            BarEntry(2f, 70f), // CLO3
            BarEntry(3f, 75f), // CLO4
            BarEntry(4f, 80f)  // CLO5
        )

        val dataSet = BarDataSet(entries, "CLO Marks")
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color2)
        )

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.xAxis.valueFormatter = CustomXAxisValueFormatter()
        barChart.xAxis.granularity = 1f
        barChart.description.text = ""
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.invalidate() // Refresh chart

        barChart.visibility = View.VISIBLE // Make chart visible
        btnShowComments.visibility = View.VISIBLE // Show "Show Comments" button
        toggleChart.visibility = View.GONE // Hide toggle when showing bar chart

        // Set click listener to render pie chart when a bar is clicked
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (isSingleCourseMode) { // Only render pie chart in Single Course mode
                    val xValue = e?.x ?: return
                    val cloLabel = when (xValue.toInt()) {
                        0 -> "CLO1"
                        1 -> "CLO2"
                        2 -> "CLO3"
                        3 -> "CLO4"
                        4 -> "CLO5"
                        else -> "Unknown CLO"
                    }
                    renderPieChart(cloLabel)
                }
            }

            override fun onNothingSelected() {
                // No action needed when nothing is selected
            }
        })
    }

    private fun renderStackedBarChartForAllCourses() {
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        isSingleCourseMode = false
        stackedBarChart.clear()

        val entries = listOf(
            BarEntry(0f, floatArrayOf(82.41f, 75.46f, 78.55f)),
            BarEntry(1f, floatArrayOf(79.78f, 82.72f, 75.46f)),
            BarEntry(2f, floatArrayOf(78.24f, 86.73f, 72.84f)),
            BarEntry(3f, floatArrayOf(75.93f, 76.7f, 78.86f)),
            BarEntry(4f, floatArrayOf(75.62f, 70.99f, 66.67f))
        )

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color2),
            ContextCompat.getColor(requireContext(), R.color.color3)
        )

        dataSet.stackLabels = arrayOf("CSE-203", "CSE-357", "CSE-353")

        val barData = BarData(dataSet)
        stackedBarChart.data = barData
        stackedBarChart.description.text = "All Courses Stacked Bar Chart"
        stackedBarChart.xAxis.valueFormatter = CustomXAxisValueFormatter()
        stackedBarChart.axisLeft.setDrawGridLines(false)
        stackedBarChart.axisRight.isEnabled = false
        stackedBarChart.xAxis.setDrawGridLines(false)
        stackedBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        barChart.xAxis.setDrawLabels(false) // Hide labels

        stackedBarChart.invalidate() // Refresh chart

        stackedBarChart.legend.isEnabled = true
        stackedBarChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        stackedBarChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        stackedBarChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        stackedBarChart.legend.setDrawInside(false)

        stackedBarChart.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        toggleChart.visibility = View.GONE // No toggle for "All Courses"
    }

    private fun renderPieChart(incourseType: String) {
        val entries = listOf(
            PieEntry(23f, "80%-100%"), // Value only, no label
            PieEntry(12f, "70%-79%"),
            PieEntry(9f, "60%-69%"),
            PieEntry(7f, "50%-59%"),
            PieEntry(2f, "40%-49%"),
            PieEntry(1f, "<40%")
        )
        val dataSet = PieDataSet(entries, "") // No label on pie chart
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color2),
            ContextCompat.getColor(requireContext(), R.color.color3),
            ContextCompat.getColor(requireContext(), R.color.color4),
            ContextCompat.getColor(requireContext(), R.color.color5),
            ContextCompat.getColor(requireContext(), R.color.color6)
        )
        val pieData = PieData(dataSet)
        pieData.setDrawValues(true) // Show values only
        pieChart.data = pieData
        pieChart.setDrawEntryLabels(false)
        pieChart.description.text = incourseType // Set description as selected incourse type
        pieChart.invalidate() // Refresh chart

        pieChart.visibility = View.VISIBLE // Make chart visible
        btnShowComments.visibility = View.VISIBLE // Show "Show Comments" button
        toggleChart.visibility = View.VISIBLE // Show toggle when pie chart is rendered

        if (isSingleCourseMode) {
            barChart.visibility = View.VISIBLE
        }

        // Set listener for the toggle switch
        toggleChart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                renderStackedBarChart(incourseType) // Render stacked bar chart if toggled on
                pieChart.visibility = View.GONE // Hide pie chart when stacked bar chart is shown
            } else {
                pieChart.visibility = View.VISIBLE // Show pie chart when toggled off
                stackedBarChart.visibility = View.GONE // Hide stacked bar chart
            }
        }
    }

    private fun renderStackedBarChart(incourseType: String) {
        val entries = listOf(
            BarEntry(0f, floatArrayOf(23f, 12f, 9f, 7f, 2f, 1f)),
        )
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color2),
            ContextCompat.getColor(requireContext(), R.color.color3),
            ContextCompat.getColor(requireContext(), R.color.color4),
            ContextCompat.getColor(requireContext(), R.color.color5),
            ContextCompat.getColor(requireContext(), R.color.color6)
        )

        dataSet.stackLabels = arrayOf("80-100%", "70-79%", "60-69%", "50-59%", "40-49%", "<40%")

        val barData = BarData(dataSet)
        stackedBarChart.data = barData
        stackedBarChart.description.text = incourseType
        stackedBarChart.axisLeft.setDrawGridLines(false)
        stackedBarChart.axisRight.isEnabled = false
        stackedBarChart.xAxis.setDrawGridLines(false)
        stackedBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        stackedBarChart.xAxis.setDrawLabels(false)
        stackedBarChart.invalidate() // Refresh chart

        stackedBarChart.legend.isEnabled = true
        stackedBarChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        stackedBarChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        stackedBarChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        stackedBarChart.legend.setDrawInside(false)

        stackedBarChart.visibility = View.VISIBLE // Show stacked bar chart
        pieChart.visibility = View.GONE // Hide pie chart when stacked bar chart is shown
        toggleChart.isChecked = true // Keep the toggle checked when showing stacked bar chart
        if (isSingleCourseMode) {
            barChart.visibility = View.VISIBLE
        }
    }
}
