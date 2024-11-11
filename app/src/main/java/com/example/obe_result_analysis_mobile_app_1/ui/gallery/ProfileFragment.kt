package com.example.obe_result_analysis_mobile_app_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val phoneTextView = view.findViewById<TextView>(R.id.phoneTextView)

        // Get current user details from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            // Set the display name and email
            nameTextView.text = user.displayName ?: "N/A"
            emailTextView.text = user.email ?: "N/A"
            phoneTextView.text = user.phoneNumber ?: "Phone number not available"

            // Load the profile picture
            val photoUrl = user.photoUrl
            photoUrl?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(profileImageView)
            }
        }

        return view
    }
}