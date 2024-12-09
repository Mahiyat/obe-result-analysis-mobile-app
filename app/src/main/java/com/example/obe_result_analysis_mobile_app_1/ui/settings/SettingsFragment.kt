package com.example.obe_result_analysis_mobile_app_1.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.obe_result_analysis_mobile_app_1.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val PHONE_NUMBER = "01645501444" // Hardcoded phone number
        private const val EMAIL_ADDRESS = "jucse29.402@gmail.com" // Hardcoded email address
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Phone preference click listener
        val phonePreference = findPreference<Preference>("phone")
        phonePreference?.setOnPreferenceClickListener {
            // Create an intent to open the dialer with the hardcoded phone number
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$PHONE_NUMBER")
            }
            startActivity(dialIntent)
            true
        }

        // Email preference click listener
        val emailPreference = findPreference<Preference>("email")
        emailPreference?.setOnPreferenceClickListener {
            // Create an intent to open the email app with the hardcoded email address
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$EMAIL_ADDRESS")
            }
            startActivity(Intent.createChooser(emailIntent, "Send email"))
            true
        }

        // Set click listener for the privacy policy preference
        val privacyPolicyPreference: Preference? = findPreference("privacy_policy")
        privacyPolicyPreference?.setOnPreferenceClickListener {
            openFragment("privacy")
            true
        }

        // Set click listener for the terms of service preference
        val termsOfServicePreference: Preference? = findPreference("terms_of_service")
        termsOfServicePreference?.setOnPreferenceClickListener {
            openFragment("terms")
            true
        }

    }

    private fun openFragment(fragment: String) {
        if (fragment == "privacy") {
            findNavController().navigate(R.id.action_settings_to_privacy_policy)
        }
        else {
            findNavController().navigate(R.id.action_settings_to_terms_of_service)
        }
    }

}