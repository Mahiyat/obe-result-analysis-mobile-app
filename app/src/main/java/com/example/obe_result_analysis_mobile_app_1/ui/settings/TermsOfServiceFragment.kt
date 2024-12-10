package com.example.obe_result_analysis_mobile_app_1.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.obe_result_analysis_mobile_app_1.R

class TermsOfServiceFragment : Fragment(R.layout.fragment_terms_of_service) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val termsOfServiceWebView: WebView = view.findViewById(R.id.termsOfServiceWebView)

        termsOfServiceWebView.setBackgroundColor(Color.parseColor("#e3f2fd"))

        val termsOfServiceHtml = """
            <html>
                <body>
                    <p>By using the App, you agree to these Terms of Service.</p>
                    <h3>Acceptance of Terms</h3>
                    <ul>
                        <li>By using the App, you agree to these Terms of Service.</li>
                        <li>You will have to login to the App using your Gmail account. This will share your name, email address and profile picture with the app.</li>
                    </ul>
                    <h3>Prohibited Conduct</h3>
                    <p>You agree not to use the App for any illegal or unauthorized purposes.</p>
                    <h3>Limitation of Liability</h3>
                    <p>We will not be liable for any damages arising from your use of the App.</p>
                </body>
            </html>
        """

        termsOfServiceWebView.loadData(termsOfServiceHtml, "text/html", "UTF-8")
    }
}
