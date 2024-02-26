package com.example.clapingo_assignment.data.api

import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackResponse
import com.example.clapingo_assignment.data.model.postFeedbackData.Payload
import com.example.clapingo_assignment.data.model.postFeedbackData.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitApiInterface {
    @GET("/api/rating/getFeedbackData")
    fun getFeedbackData(): Call<FeedbackResponse>
}