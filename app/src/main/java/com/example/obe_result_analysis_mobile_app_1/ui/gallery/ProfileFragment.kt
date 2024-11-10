package com.example.obe_result_analysis_mobile_app_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //val nameTextView = view.findViewById<TextView>(R.id.profileName)
        //val emailTextView = view.findViewById<TextView>(R.id.profileEmail)

        // Get current user details from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
        }

        return view
    }
}