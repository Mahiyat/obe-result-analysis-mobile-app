# OBE Insight Mobile App

OBE Insight is a mobile application designed to enhance the accessibility and usability of the Outcome-Based Education (OBE) Result Analysis System. Built for Android, this app empowers users to analyze academic performance, streamline communication, and receive actionable insights in real-time.

---

## Terminologies
* OBE: Outcome Based Education
* CIE: Continuous Internal Evaluation
* SEE: Semester End Examination
* CLO: Course Learning Outcome

## Features

- **Single-Sign-On (SSO)**: Secure login via Google account
- **Dashboard**: A dynamic interface displaying course statistics and pending activities
- **Course Details**: Comprehensive evaluation metrics, including CIE and SEE analytics
- **AI-Generated Insights**: AI-Generated Insights: Automatic interpretation of academic trends using visualizations like bar and pie charts
- **Messaging System**: Seamless communication with academic authorities and exam offices
- **PDF Generation**: Export PDF of the charts and AI-generated comments.

---

## Technological Stack

- **Frontend**: Kotlin with native Android UI components.
- **Backend**: Django REST APIs integrated with SQLite database.
- **Charts & Visualization**: Third-party libraries for graph rendering.
- **Authentication**: Single-Sign-On (SSO) using Firebase authentication.

---

## Target Users

1. **Exam Office**: Administrative data management and updates.
2. **Exam Committees**: Oversight of academic operations and evaluations.
3. **Course Teachers**: Tracking and analyzing student performance.
4. **Students**: Personal academic progress and insights.

---

## Setup and Installation

1. **Prerequisites**:
    - Android Studio (latest version)
    - Android Device (Minimum version: Android 8.0 Oreo)
    - Django REST API server for backend integration.

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repository/obe-insight.git

3. **Open in Android Studio**:
- Open the cloned project in Android Studio.
- Sync the Gradle files.

4. **Configure Backend**:
- Set up the Django REST API server with the required SQLite database. Web app project link: https://github.com/Mahiyat/obe-project-result-analysis.git
- Ensure the server is running and accessible.

5. ** Setup Ollama **:
- Setup ollama as per the instructions [here](https://github.com/ollama/ollama)

6. **Run the App**:
- Connect your Android device or start an emulator.
- Build and run the project from Android Studio.

---

## What's Next

[ ] Feature implementation for Student, Exam Committee, and Exam Office
[ ] Implementation of Role Based Access Control (RBAC)
[ ] Integration of REST APIs for marks entry and result statistics
[ ] Enhancement of messaging system
[ ] Integrating AI-chat system for result analysis and improvement suggestions
[ ] Enhancement of the contents of exported PDF



