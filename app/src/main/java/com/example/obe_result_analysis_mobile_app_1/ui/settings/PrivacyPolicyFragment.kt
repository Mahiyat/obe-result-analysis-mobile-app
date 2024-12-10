package com.example.obe_result_analysis_mobile_app_1.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.obe_result_analysis_mobile_app_1.R

class PrivacyPolicyFragment : Fragment(R.layout.fragment_privacy_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val privacyPolicyWebView: WebView = view.findViewById(R.id.privacyPolicyWebView)

        privacyPolicyWebView.setBackgroundColor(Color.parseColor("#e3f2fd"))

        val privacyPolicyHtml = """
            <html>
                <body>
                    <p>This Privacy Policy describes how OBE Insight collects, uses, discloses, and protects your information.</p>
                    <h3>Information We Collect</h3>
                    <ul>
                        <li>The App collects information you provide when you use the messaging feature, including messages and recipient information.</li>
                        <li>The app collects data about your courses, including CIE and SEE marksheets, student performance, and related academic data.</li>
                        <li>The App may collect device information, such as your device model, operating system, and unique device identifiers.</li>
                        <li>The App collects your name, email address, and profile picture from your Gmail account.</li>
                    </ul>
                    <h3>How We Use Your Information</h3>
                    <ul>
                        <li>To facilitate communication between users and academic authorities through the messaging system.</li>
                        <li>To provide and display relevant course information, including evaluation marksheets and student performance data.</li>
                        <li>To send notifications about course and student matters.</li>
                        <li>To improve the App's functionality and user experience.</li>
                        <li>To analyze app usage trends and statistics.</li>
                    </ul>
                    <h3>Data Sharing and Disclosure</h3>
                    <ul>
                        <li>The App may share your information with the exam office, exam committees, and course teachers as necessary to facilitate the OBE process.</li>
                        <li>We may disclose your information if required by law or to protect the rights, property, and safety of ourselves or others.</li>
                    </ul>
                    <h3>Data Security</h3>
                    <p>The App employs security measures to protect your data, including encryption and user authentication.</p>
                    <h3>Data Retention</h3>
                    <p>The App will retain your data for as long as necessary to provide you with the services and fulfill the purposes described in this Privacy Policy.</p>
                    <h3>Your Choices</h3>
                    <ul>
                        <li>You may choose not to use the messaging feature if you do not want to share your communications through the app.</li>
                        <li>You may contact us to request access to, correction of, or deletion of your personal information.</li>
                    </ul>
                    <h3>Updates to this Privacy Policy</h3>
                    <p>We may update this Privacy Policy from time to time. We will notify you of any material changes through the App and email.</p>
                </body>
            </html>
        """

        privacyPolicyWebView.loadData(privacyPolicyHtml, "text/html", "UTF-8")
    }
}
