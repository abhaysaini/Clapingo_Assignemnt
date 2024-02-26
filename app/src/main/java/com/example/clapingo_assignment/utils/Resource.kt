package com.example.clapingo_assignment.utils

sealed class Resource<T>(val responseData: T? = null, val message: String? = null) {
    class Success<T>(responseList: T) : Resource<T>(responseList, null)
    class Error<T>(message: String) : Resource<T>(null, message)
    class Loading<T> : Resource<T>()
}