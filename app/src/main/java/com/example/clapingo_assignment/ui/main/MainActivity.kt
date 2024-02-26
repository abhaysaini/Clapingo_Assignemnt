package com.example.clapingo_assignment.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clapingo_assignment.R
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackCategory
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackResponse
import com.example.clapingo_assignment.data.model.postFeedbackData.Payload
import com.example.clapingo_assignment.databinding.ActivityMainBinding
import com.example.clapingo_assignment.ui.adapter.ParentAdapter
import com.example.clapingo_assignment.ui.helper.PayloadInterface
import com.example.clapingo_assignment.ui.main.viewModel.MainViewModel
import com.example.clapingo_assignment.ui.submitfeedback.SubmitFeedbackActivity
import com.example.clapingo_assignment.utils.Constants.POST_URL
import com.example.clapingo_assignment.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PayloadInterface {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var parentAdapter: ParentAdapter
    private var payloadList: MutableMap<String, Payload> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerview()
        observeUI()
        binding.submitButton.setOnClickListener {
            if(payloadList.isEmpty()){
                binding.thirdT.text  = getString(R.string.Error)
                binding.thirdT.setTextColor(ContextCompat.getColor(
                    this,
                    R.color.red
                ))
            }
        }
    }

    private fun observeUI() {
        viewModel.responseLiveData.observe(this@MainActivity) { response ->
            when (response) {
                is Resource.Success -> handleSuccessResponse(response)
                is Resource.Error -> handleError(response)
                is Resource.Loading -> handleLoading()
            }
        }
    }

    private fun handleSuccessResponse(response: Resource.Success<FeedbackResponse>) {
        runOnUiThread {
            response.responseData?.let { data ->
                data.feedbackCategories?.add(
                    FeedbackCategory(
                        "Other",
                        emptyList()
                    )
                )
                parentAdapter = ParentAdapter(
                    context = this@MainActivity,
                    feedbackCategory = data.feedbackCategories,
                    payloadInterface = this@MainActivity
                )
                binding.parentRecyclerView.adapter = parentAdapter
            }
        }
    }

    private fun handleError(response: Resource.Error<FeedbackResponse>) {
        Snackbar.make(binding.root, "Check your network", Snackbar.LENGTH_SHORT).show()
    }

    private fun handleLoading() {
        // Optionally, you can add loading UI indicators here
    }


    private fun setUpRecyclerview() {
        binding.parentRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            viewModel.getRequestResponse()
        }
    }


    override fun updatePayload(listOfPayload: MutableMap<String, Payload>) {
        payloadList = listOfPayload
        binding.submitButton.setOnClickListener {
            showLoadingOverlay(true)

            val jsonPayload = JsonObject()
            listOfPayload.forEach { (_, payload) ->
                if (!jsonPayload.has(payload.category)) {
                    jsonPayload.add(payload.category, JsonObject())
                }
                val categoryObject = jsonPayload.getAsJsonObject(payload.category)
                if (!categoryObject.has(payload.type)) {
                    categoryObject.add(payload.type, JsonObject())
                }
                val aspectObject = categoryObject.getAsJsonObject(payload.type)
                aspectObject.add(payload.aspect, Gson().toJsonTree(payload.list))
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val client = OkHttpClient()
                    val mediaType = VALUE.toMediaType()
                    val body = jsonPayload.toString().toRequestBody(mediaType)
                    val request = Request.Builder()
                        .url(POST_URL)
                        .post(body)
                        .addHeader(CONTENT_TYPE, VALUE)
                        .build()
                    val response = client.newCall(request).execute()
                    val responseBodyString = response.body?.string()
                    val jsonObject = responseBodyString?.let { JSONObject(it) }
                    val message = jsonObject?.getString("message")
                    val code = response.code

                    delay(MIN_LOADING_DURATION)
                    navigateToSubmitFeedback(message, code)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    showLoadingOverlay(false)
                }
            }
        }
    }

    private fun showLoadingOverlay(show: Boolean) {
        if (show) {
            binding.loadingAnimation.loadingOverlay.visibility = View.VISIBLE
            binding.loadingAnimation.loadingOverlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        } else {
            binding.loadingAnimation.loadingOverlay.visibility = View.GONE
            binding.loadingAnimation.loadingOverlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
        }
    }

    private fun navigateToSubmitFeedback(message: String?, code: Int) {
        Intent(this@MainActivity, SubmitFeedbackActivity::class.java).apply {
            putExtra("message", message)
            putExtra("code", code)
            startActivity(this)
            finish()
        }
    }
    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val VALUE = "application/json"
        const val MIN_LOADING_DURATION = 500L
    }
}