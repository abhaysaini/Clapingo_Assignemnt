package com.example.clapingo_assignment.data.model.getFeedbackData

data class FeedbackItems(
    val aspect: String,
    val didWell: List<String>,
    val scopeOfImprovement: List<String>
)
