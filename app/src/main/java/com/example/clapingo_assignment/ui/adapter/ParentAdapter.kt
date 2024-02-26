package com.example.clapingo_assignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clapingo_assignment.R
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackCategory
import com.example.clapingo_assignment.data.model.postFeedbackData.Payload
import com.example.clapingo_assignment.databinding.ItemFeedbackBinding
import com.example.clapingo_assignment.ui.bottomsheet.BottomSheetDialog
import com.example.clapingo_assignment.ui.helper.ObserveInterface
import com.example.clapingo_assignment.ui.helper.PayloadInterface

class ParentAdapter(
    val context: Context,
    private val feedbackCategory: MutableList<FeedbackCategory>?,
    private val payloadInterface: PayloadInterface
) :
    RecyclerView.Adapter<ParentAdapter.ViewHolder>(), ObserveInterface,
    BottomSheetDialog.PayloadInterface {

    private lateinit var binding: ItemFeedbackBinding
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    inner class ViewHolder(private val binding: ItemFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(feedbackCategory: FeedbackCategory?, position: Int) {
            binding.categoryName.text = feedbackCategory?.categoryName
            when (feedbackCategory?.categoryName) {
                CONFIDENCE -> {
                    binding.imageView.setImageResource(R.drawable.ic_confidence)
                }

                GRAMMAR -> {
                    binding.imageView.setImageResource(R.drawable.ic_grammar)
                }

                FLUENCY_AND_VOCAB -> {
                    binding.imageView.setImageResource(R.drawable.ic_fluency)
                }

                PRONUNCIATION -> {
                    binding.imageView.setImageResource(R.drawable.ic_pronunciation)
                }

                else -> {
                    binding.imageView.setImageResource(R.drawable.ic_others)
                }
            }

            val isExpanded = position == selectedItemPosition
            binding.imageViewR.setImageResource(if (isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_right)
            binding.recyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.textCardView.visibility =
                if (feedbackCategory?.categoryName == "Other" && isExpanded) View.VISIBLE else View.GONE

            binding.linearLayoutParent.setOnClickListener {
                val previousSelected = selectedItemPosition
                selectedItemPosition =
                    if (selectedItemPosition == bindingAdapterPosition) RecyclerView.NO_POSITION else bindingAdapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(bindingAdapterPosition)
            }

            if (isExpanded) {
                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                feedbackCategory?.let { category ->
                    binding.recyclerView.adapter = SubParentAdapter(
                        context,
                        category.feedbackItems,
                        this@ParentAdapter,
                        category.categoryName
                    )
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedbackCategory = feedbackCategory?.get(position)
        holder.bind(feedbackCategory, position)
    }

    override fun getItemCount(): Int {
        return feedbackCategory?.size ?: 0
    }


    override fun initializer(
        data: List<String>,
        position: Int,
        aspect: String,
        categoryName: String,
        isType: String
    ) {
        val bottomSheetDialog =
            BottomSheetDialog(context, data, aspect, categoryName, isType, this)
        bottomSheetDialog.show(
            (context as AppCompatActivity).supportFragmentManager,
            bottomSheetDialog.tag
        )
    }

    private val payloadsMapping: MutableMap<String, Payload> = mutableMapOf()

    override fun updatePayload(payload: Payload) {
        payloadsMapping[payload.aspect] = payload
        payloadInterface.updatePayload(payloadsMapping)
    }

    companion object {
        const val CONFIDENCE = "Confidence"
        const val GRAMMAR = "Grammar"
        const val FLUENCY_AND_VOCAB = "Fluency And Vocabulary"
        const val PRONUNCIATION = "Pronunciation"
    }
}