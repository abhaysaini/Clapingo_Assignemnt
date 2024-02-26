package com.example.clapingo_assignment.data.repository

import com.example.clapingo_assignment.data.api.RetrofitApiInterface
import com.example.clapingo_assignment.data.api.RetrofitHelper
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackResponse
import com.example.clapingo_assignment.data.model.postFeedbackData.Payload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject


class MainRepository @Inject constructor(private  val apiInterface: RetrofitApiInterface) {

    suspend fun getFeedbackResponseUI(): FeedbackResponse {
        return withContext(Dispatchers.IO) {
            apiInterface.getFeedbackData().execute().body()
                ?: throw IOException(ERROR)
        }
    }

    companion object{
        const val ERROR = "Error fetching Data"
    }
}