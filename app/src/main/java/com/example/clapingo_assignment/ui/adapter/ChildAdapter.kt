package com.example.clapingo_assignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clapingo_assignment.R
import com.example.clapingo_assignment.databinding.ItemBoxBinding

class ChildAdapter(private val context: Context, private val dataList: List<String>?) :
    RecyclerView.Adapter<ChildAdapter.ViewHolder>() {

    val selectedItems = mutableListOf<String>()

    inner class ViewHolder(private val binding: ItemBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String?, position: Int) {
            item?.let {
                binding.specifics.text = it
            } ?: run {
                println("Null string at position $position")
            }

            binding.specifics.setOnClickListener {
                val selectedItem = item ?: return@setOnClickListener
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem)
                    updateItemBackground(binding, false)
                } else {
                    selectedItems.add(selectedItem)
                    updateItemBackground(binding, true)
                }
            }
        }

        private fun updateItemBackground(binding: ItemBoxBinding, isSelected: Boolean) {
            val colorResId = if (isSelected) R.color.light_green else android.R.color.transparent
            val color = ContextCompat.getColor(context, colorResId)
            binding.specifics.setBackgroundColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBoxBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList?.get(position), position)
    }
}
