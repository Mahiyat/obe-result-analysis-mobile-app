package com.example.obe_result_analysis_mobile_app_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Set click listener for the privacy policy preference
        val privacyPolicyPreference: Preference? = findPreference("privacy_policy")
        privacyPolicyPreference?.setOnPreferenceClickListener {
            openWebPage("https://juniv.edu")
            true
        }

        // Set click listener for the terms of service preference
        val termsOfServicePreference: Preference? = findPreference("terms_of_service")
        termsOfServicePreference?.setOnPreferenceClickListener {
            openWebPage("https://juniv.edu")
            true
        }

        // Set click listener for the logout preference
        val logoutPreference: Preference? = findPreference("logout")
        logoutPreference?.setOnPreferenceClickListener {
            // Implement your logout logic here
            performLogout()
            true
        }

        // Set click listener for the change password preference
        val changePasswordPreference: Preference? = findPreference("change_password")
        changePasswordPreference?.setOnPreferenceClickListener {
            // Implement your change password logic here
            true
        }
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (activity?.packageManager?.let { it.resolveActivity(intent, 0) } != null) {
            startActivity(intent)
        }
    }

    private fun performLogout() {
        val context = requireContext()

        // Configure Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                // After access is revoked, navigate to the Sign-In screen
                val intent = Intent(context, FirebaseSignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}