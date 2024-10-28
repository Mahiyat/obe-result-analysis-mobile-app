package com.example.obe_result_analysis_mobile_app_1.ui.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.obe_result_analysis_mobile_app_1.R


class MarksUpdateRequestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marks_update_request, container, false)

        val subjectEditText = view.findViewById<EditText>(R.id.subjectEditText2)
        val examNameEditText = view.findViewById<EditText>(R.id.examNameEditText2)
        val courseEditText = view.findViewById<EditText>(R.id.courseEditText2)
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText2)
        val sendButton = view.findViewById<Button>(R.id.sendButton2)

        sendButton.setOnClickListener {
            val subject = subjectEditText.text.toString()
            val examName = examNameEditText.text.toString()
            val course = courseEditText.text.toString()
            val message = messageEditText.text.toString()

            // Handle the send action (e.g., submit to server, show Toast, etc.)
            Toast.makeText(requireContext(), "Request Sent", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
