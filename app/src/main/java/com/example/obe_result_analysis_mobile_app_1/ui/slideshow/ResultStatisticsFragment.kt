package com.example.obe_result_analysis_mobile_app_1.ui.slideshow

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import io.noties.markwon.Markwon
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import java.io.File
import java.io.FileOutputStream

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
    private lateinit var toggleChart: Switch
    private lateinit var commentTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnExportPDF: Button

    private var courseSelected = false
    private var examTitleSelected = false
    private var incourseTypeSelected = false
    private var isSingleCourseMode = false

    private lateinit var incourseName: String
    private lateinit var course: String
    private lateinit var exam: String

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(200, TimeUnit.SECONDS)
        .readTimeout(200, TimeUnit.SECONDS)
        .writeTimeout(200, TimeUnit.SECONDS)
        .build()


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
        toggleChart = view.findViewById(R.id.toggle_chart)
        commentTextView = view.findViewById(R.id.commentTextView)
        progressBar = view.findViewById(R.id.progressBar)
        btnExportPDF = view.findViewById(R.id.btn_export_pdf)


        setupEvaluationSpinner()
        setupCharts()

        btnShowComments.setOnClickListener {
            // Define the action when the button is clicked
            commentTextView.text=""
            showComments()
        }

        btnExportPDF.setOnClickListener{
            exportAllVisibleComponentsToPDF()
        }

        return view
    }

    private fun showComments() {
        progressBar.visibility = View.VISIBLE
        commentTextView.visibility = View.VISIBLE

        if(incourseTypeSelected) {
            val performance = listOf(23, 12, 9, 7, 2, 1)
            val prompt = "Comment on the data $performance which is the number of students among 54 students in the ranges 80%-100%, 70%-79%, 60%-69%, 50%-59%, 40%-49%, and less than 40% respectively in a course in $incourseName exam. Also give your opinion of any improvements if needed."
            sendRequest(prompt, commentTextView)

        }

        else {
            val performance = listOf(85, 90, 70, 75, 80)
            val prompt = "Comment on the data $performance which is respectively the overall performance (in percentage) of 54 students in the CLOs (Course Learning Outcomes) CLO1 (Remember), CLO2 (Understand), CLO3 (Apply), CLO4 (Analyze), and CLO5 (Evaluate) of the Semester End Examination  in a course with course code $course and exam title $exam. Also give your opinion of any improvements if needed. Note that the minimum performance acceptable is 60% for all the CLOs."
            sendRequest(prompt, commentTextView)
        }
    }

    private fun sendRequest(query: String, outputTextView: TextView) {
        val url = "http://localhost:11434/api/generate"
        val json = """
            {
                "model": "llama3.2",
                "prompt": "$query",
                "stream": true,
                "options": {
                    "seed": 42,
                    "temperature": 0.5
                }
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ResultStatisticsFragment", "Failed to connect: ${e.message}")
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                    outputTextView.setText("Failed to generate response!")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                }

                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        val stringBuilder = StringBuilder()

                        try {
                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                val chunk = String(buffer, 0, bytesRead)
                                val jsonResponse = chunk
                                try{
                                    val jsonObject = JSONObject(jsonResponse)
                                    val generatedText = jsonObject.getString("response")
                                    val markwonComment = context?.let { Markwon.create(it) }
                                    stringBuilder.append(generatedText)
                                    activity?.runOnUiThread {
//                                        outputTextView.text = stringBuilder.toString()
                                        markwonComment?.setMarkdown(outputTextView, stringBuilder.toString())
                                    }
                                } catch (e: Exception){
                                    Log.e("ResultStatisticsFragment", "Error parsing stream response: ${e.message}")
                                }
                            }
//                            activity?.runOnUiThread {
//                                val markwonComment = context?.let { Markwon.create(it) }
//                                markwonComment?.setMarkdown(outputTextView, stringBuilder.toString())
//                            }
                        } catch (e: Exception) {
                            Log.e("ResultStatisticsFragment", "Error reading stream: ${e.message}")
                            activity?.runOnUiThread {
                                outputTextView.text = getString(R.string.error_message, e.message)
                            }
                        }
                    } ?: activity?.runOnUiThread {
                        outputTextView.setText("Failed to generate response!")
                    }
                } else {
                    val responseBody = response.body?.string()
                    try {
                        val jsonObject = responseBody?.let { JSONObject(it) }
                        val errorMessage = jsonObject?.getJSONObject("error")?.getString("message")
                        Log.e("ResultStatisticsFragment", "API error: $errorMessage")
                        activity?.runOnUiThread {
                            outputTextView.text = getString(R.string.error_message, errorMessage)
                        }
                    } catch (e: Exception) {
                        Log.e("ResultStatisticsFragment", "Error parsing error response: ${e.message}")
                        activity?.runOnUiThread {
                            outputTextView.text = getString(R.string.error_message, e.message)
                        }
                    }
                }
            }
        })
    }


    private fun refreshPage() {

        evaluationTypeSpinner.setSelection(0)
        seeSpinner.setSelection(0)
        courseSpinner.setSelection(0)
        examTitleSpinner.setSelection(0)
        incourseTypeSpinner.setSelection(0)

        seeSpinner.visibility = View.GONE
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        incourseTypeSpinner.visibility = View.GONE


        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        stackedBarChart.visibility = View.GONE


        btnShowComments.visibility = View.GONE
        btnExportPDF.visibility = View.GONE
        toggleChart.visibility = View.GONE


        courseSelected = false
        examTitleSelected = false
        incourseTypeSelected = false


        barChart.clear()
        pieChart.clear()
        stackedBarChart.clear()

        commentTextView.text=""
        commentTextView.visibility = View.GONE

        incourseName=""
        course=""
        exam=""

        progressBar.visibility = View.GONE
    }

    private fun setupEvaluationSpinner() {
        commentTextView.text=""
        btnExportPDF.visibility = View.GONE
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
        commentTextView.text=""
        btnExportPDF.visibility = View.GONE
        courseSpinner.setSelection(0)
        examTitleSpinner.setSelection(0)
        incourseTypeSpinner.setSelection(0)
        val courses = listOf("Select a Course", "CSE-203", "CSE-357", "CSE-353")
        val exams = listOf("Select Exam Title", "2nd Year 1st Semester 2020", "3rd Year 2nd Semester 2020", "3rd Year 2nd Semester 2021")
        val incourseTypes = listOf("Select Incourse Type", "Tutorial", "Assignment", "Quiz", "Curricular/Co-curricular Activities")

        courseSpinner.visibility = View.VISIBLE
        examTitleSpinner.visibility = View.VISIBLE
        incourseTypeSpinner.visibility = View.GONE
        seeSpinner.visibility = View.GONE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        stackedBarChart.visibility = View.GONE
        btnShowComments.visibility = View.GONE
        toggleChart.visibility = View.GONE
        commentTextView.visibility = View.GONE

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
                incourseName = incourseTypes[position]
                renderPieChart(incourseTypes[position])
                toggleChart.visibility = View.VISIBLE
            }
        }
    }

    private fun checkCIESelections(exams: List<String>, incourseTypes: List<String>) {
        if (courseSelected && examTitleSelected) {
            incourseTypeSpinner.visibility = View.VISIBLE
        } else {
            incourseTypeSpinner.visibility = View.GONE
        }
    }

    private fun showSEEOptions() {
        commentTextView.text=""
        btnExportPDF.visibility = View.GONE
        incourseTypeSelected=false
        val seeOptions = listOf("Select SEE Type", "Single Course", "All Courses")

        // Populate SEE type spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seeSpinner.adapter = adapter


        seeSpinner.visibility = View.VISIBLE
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        incourseTypeSpinner.visibility = View.GONE
        barChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        stackedBarChart.visibility = View.GONE
        btnShowComments.visibility = View.GONE
        toggleChart.visibility = View.GONE

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
                if (position == 0) return
                onSelectAction(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCharts() {
        // Setup initial chart display (empty)
    }

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
                    course=coursesSEE[coursePosition]
                    exam=examsSEE[examPosition]
                    renderBarChartForSingleCourse()
                }
            }
        }
    }

    private fun renderBarChartForSingleCourse() {
        btnExportPDF.visibility = View.VISIBLE
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
        barChart.invalidate()

        barChart.visibility = View.VISIBLE
        btnShowComments.visibility = View.VISIBLE
        toggleChart.visibility = View.GONE

        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (isSingleCourseMode) {
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

            }
        })
    }

    private fun renderStackedBarChartForAllCourses() {
        btnExportPDF.visibility = View.VISIBLE
        courseSpinner.visibility = View.GONE
        examTitleSpinner.visibility = View.GONE
        isSingleCourseMode = false
        btnShowComments.visibility = View.GONE
        commentTextView.text=""
        commentTextView.visibility=View.GONE
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
        toggleChart.visibility = View.GONE
    }

    private fun renderPieChart(incourseType: String) {
        btnExportPDF.visibility = View.VISIBLE
        val entries = listOf(
            PieEntry(23f, "80%-100%"),
            PieEntry(12f, "70%-79%"),
            PieEntry(9f, "60%-69%"),
            PieEntry(7f, "50%-59%"),
            PieEntry(2f, "40%-49%"),
            PieEntry(1f, "<40%")
        )
        val dataSet = PieDataSet(entries, "")
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
        btnExportPDF.visibility = View.VISIBLE
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

    private fun exportAllVisibleComponentsToPDF() {
        val pdfDocument = PdfDocument()
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile: File

        if (incourseTypeSelected) {
            outputFile = File(downloadsDir, "Report$incourseName.pdf")
        } else {
            outputFile = File(downloadsDir, "Report$course.pdf")
        }

        try {
            // Get screen height dynamically
            val screenHeight = resources.displayMetrics.heightPixels
            val width = resources.displayMetrics.widthPixels // Screen width in pixels

            val visibleComponents = listOf(
                barChart,
                pieChart,
                stackedBarChart,
            )

            var currentY = 20f // Starting Y-coordinate
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 20f
                textAlign = Paint.Align.LEFT
            }

            // Function to create a new page and start drawing
            fun createNewPage(): PdfDocument.Page {
                val pageInfo = PdfDocument.PageInfo.Builder(width, screenHeight, 1).create()
                return pdfDocument.startPage(pageInfo) // Start a new page
            }

            var totalHeight = 0

            // First page creation
            var currentPage = createNewPage()

            visibleComponents.forEach { chart ->
                if (chart.visibility == View.VISIBLE) {
                    val chartBitmap = Bitmap.createBitmap(chart.width, chart.height, Bitmap.Config.ARGB_8888)
                    val chartCanvas = Canvas(chartBitmap)
                    chart.draw(chartCanvas)

                    // Check if the current content fits on the page
                    if (currentY + chart.height + 100 > screenHeight) {
                        // Content overflows, create a new page
                        pdfDocument.finishPage(currentPage) // Finish the current page
                        currentPage = createNewPage() // Start a new page
                        currentY = 20f
                    }

                    // Draw chart on the current page
                    currentPage.canvas.drawBitmap(chartBitmap, 0f, currentY, null)
                    currentY += chart.height + 20 // Update Y position for the next component
                }
            }

            // Draw comment if visible
            if (commentTextView.visibility == View.VISIBLE) {
                val commentText = "Generated Comment:"
                val pageWidth = width.toFloat()

                // Draw "Generated Comment" header
                currentPage.canvas.drawText(commentText, 20f, currentY, paint)
                currentY += 20f // Move down after the header text

                // Split comment text into words
                val words = commentTextView.text.split(" ")

                // Wrap text to fit within the page width
                val wrappedLines = mutableListOf<String>()
                var currentLine = StringBuilder()

                for (word in words) {
                    val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                    if (paint.measureText(testLine) < pageWidth - 40f) {
                        currentLine.append(if (currentLine.isEmpty()) word else " $word")
                    } else {
                        wrappedLines.add(currentLine.toString())
                        currentLine = StringBuilder(word) // Start a new line with the current word
                    }
                }
                // Add the last line if it's not empty
                if (currentLine.isNotEmpty()) {
                    wrappedLines.add(currentLine.toString())
                }

                // Write wrapped lines to the page
                for (line in wrappedLines) {
                    if (currentY + 20 > screenHeight) {
                        pdfDocument.finishPage(currentPage) // Finish current page
                        currentPage = createNewPage() // Start a new page
                        currentY = 20f // Reset to top of the new page
                    }
                    currentPage.canvas.drawText(line, 20f, currentY, paint)
                    currentY += 20f // Move down for the next line
                }
            }

            // Finish the last page
            pdfDocument.finishPage(currentPage)

            // Write the PDF to file
            FileOutputStream(outputFile).use { pdfDocument.writeTo(it) }

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                outputFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No PDF viewer installed!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            Log.e("PDFExport", "Error exporting PDF: ${e.message}")
        } finally {
            pdfDocument.close()
        }
    }





}
