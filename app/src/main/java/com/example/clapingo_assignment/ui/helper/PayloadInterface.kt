package com.example.clapingo_assignment.ui.helper

import com.example.clapingo_assignment.data.model.postFeedbackData.Payload

interface PayloadInterface {
    fun updatePayload(listOfPayload: MutableMap<String, Payload>)
}