package com.example.clapingo_assignment.data.model.postFeedbackData

data class Payload(
    val category: String,
    val aspect: String,
    val type: String,
    val list: List<String>
)