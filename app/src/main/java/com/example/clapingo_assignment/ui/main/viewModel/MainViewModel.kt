package com.example.clapingo_assignment.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackResponse
import com.example.clapingo_assignment.data.repository.MainRepository
import com.example.clapingo_assignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _responseLiveData = MutableLiveData<Resource<FeedbackResponse>>()
    val responseLiveData: LiveData<Resource<FeedbackResponse>> = _responseLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _responseLiveData.value = Resource.Error("An error occurred: ${exception.message}")
    }

    fun getRequestResponse() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _responseLiveData.value = Resource.Loading()
            val feedbackResponse = repository.getFeedbackResponseUI()
            _responseLiveData.value = Resource.Success(feedbackResponse)
        }
    }
}
