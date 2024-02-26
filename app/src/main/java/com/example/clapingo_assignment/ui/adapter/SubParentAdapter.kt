package com.example.clapingo_assignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clapingo_assignment.R
import com.example.clapingo_assignment.data.model.getFeedbackData.FeedbackItems
import com.example.clapingo_assignment.databinding.ItemEmojiBinding
import com.example.clapingo_assignment.ui.helper.ObserveInterface
import de.hdodenhof.circleimageview.CircleImageView

class SubParentAdapter(
    private val context: Context,
    private val data: List<FeedbackItems>?,
    private val observeInterface: ObserveInterface,
    private val feedbackCategoryName: String
) : RecyclerView.Adapter<SubParentAdapter.ViewHolder>() {

    private val feedbackCounts = MutableList(data?.size ?: 0) { 0 }
    private val selectedItems: MutableList<Int> = MutableList(data?.size ?: 0) { -1 }

    inner class ViewHolder(private val binding: ItemEmojiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(feedbackItem: FeedbackItems?, position: Int) {
            with(binding) {
                aspect.text = feedbackItem?.aspect

                positive.setOnClickListener {
                    handleCircleImageViewClick(position, positive)
                    feedbackItem?.let {
                        observeInterface.initializer(
                            it.didWell,
                            position,
                            it.aspect,
                            feedbackCategoryName,
                            DID_WELL
                        )
                    }
                }

                negative.setOnClickListener {
                    handleCircleImageViewClick(position, negative)
                    feedbackItem?.let {
                        observeInterface.initializer(
                            it.scopeOfImprovement,
                            position,
                            it.aspect,
                            feedbackCategoryName,
                            SCOPE_OF_IMPROVEMENT
                        )
                    }
                }

                if (isSelected(position)) {
                    handleCircleImageViewSelection(binding, selectedItems[position])
                } else {
                    handleCircleImageViewDeselection(binding)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data?.get(position), position)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    private fun handleCircleImageViewClick(position: Int, circleImageView: CircleImageView) {
        val prevSelected = selectedItems[position]
        selectedItems[position] = circleImageView.id
        if (prevSelected != -1) {
            feedbackCounts[position] -= 1
        }
        feedbackCounts[position] += 1
        notifyItemChanged(position)
    }

    private fun handleCircleImageViewSelection(binding: ItemEmojiBinding, selectedItemId: Int) {
        val selectedImageView =
            if (selectedItemId == binding.positive.id) binding.positive else binding.negative

        selectedImageView.borderColor = when (selectedImageView.id) {
            binding.positive.id -> ContextCompat.getColor(context, R.color.dark_green)
            binding.negative.id -> ContextCompat.getColor(context, R.color.red)
            else -> ContextCompat.getColor(context, R.color.light_green)
        }

        val deselectedImageView =
            if (selectedImageView == binding.positive) binding.negative else binding.positive
        deselectedImageView.borderColor = ContextCompat.getColor(context, R.color.light_green)
    }

    private fun handleCircleImageViewDeselection(binding: ItemEmojiBinding) {
        binding.positive.borderColor = ContextCompat.getColor(context, R.color.light_green)
        binding.negative.borderColor = ContextCompat.getColor(context, R.color.light_green)
    }

    private fun isSelected(position: Int): Boolean = selectedItems[position] != -1

    companion object {
        const val DID_WELL = "didWell"
        const val SCOPE_OF_IMPROVEMENT = "scopeOfImprovement"
    }
}
