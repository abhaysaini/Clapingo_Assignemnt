package com.example.clapingo_assignment.data.model.getFeedbackData

data class FeedbackResponse(
    val statusCode : Int,
    val feedbackCategories : MutableList<FeedbackCategory>
)