package com.example.obe_result_analysis_mobile_app_1.network

import retrofit2.Call
import retrofit2.http.GET

data class CountsResponse(val all_courses_count: Int, val pending_courses_count: Int)
data class PendingActivity(val id: Int, val course_id: String, val course_name: String, val exam_title: String, val text: String)

interface ApiService {

    @GET("api/courses/getcounts")
    fun getCounts(): Call<CountsResponse>

    @GET("api/buttons/courses/incomplete")
    fun getPendingActivities(): Call<List<PendingActivity>>
}