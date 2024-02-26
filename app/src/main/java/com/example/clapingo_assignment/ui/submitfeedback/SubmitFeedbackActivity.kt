package com.example.clapingo_assignment.ui.submitfeedback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clapingo_assignment.R
import com.example.clapingo_assignment.databinding.ActivitySubmitFeedbackBinding
import com.example.clapingo_assignment.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubmitFeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitButton.setOnClickListener {
            val intent = Intent(this@SubmitFeedbackActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}