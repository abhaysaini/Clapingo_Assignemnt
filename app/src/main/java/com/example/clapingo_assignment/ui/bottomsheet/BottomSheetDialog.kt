package com.example.clapingo_assignment.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clapingo_assignment.data.model.postFeedbackData.Payload
import com.example.clapingo_assignment.databinding.FragmentBottomSheetDilaogBinding
import com.example.clapingo_assignment.ui.adapter.ChildAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog(
    private val context: Context,
    val data: List<String>,
    private val aspect: String,
    private val categoryName: String,
    private val isType: String,
    private val payloadInterface: PayloadInterface
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDilaogBinding
    private lateinit var adapter: ChildAdapter

    interface PayloadInterface {
        fun updatePayload(payload: Payload)
    }

    private fun toFormat(input: String): String {
        val parts = input.split("(?<=[a-z])(?=[A-Z])|\\s+".toRegex())
        val camelCaseParts = parts.mapIndexed { index, part ->
            if (index == 0) part.toLowerCase() else part.capitalize()
        }
        return camelCaseParts.joinToString("")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBottomSheetDilaogBinding.inflate(layoutInflater,container,false)
        binding.childChildCardView.visibility = View.VISIBLE
        binding.bottomRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ChildAdapter(context, data)
        binding.bottomRecyclerView.adapter = adapter
        binding.imageCrossButton.setOnClickListener {
            dismiss()
        }
        val userFeedbacks = adapter.selectedItems
        binding.doneButton.setOnClickListener {
            if (userFeedbacks.size > 0) {

                val basicPayload = Payload(
                    toFormat(categoryName),
                    toFormat(aspect),
                    toFormat(isType),
                    userFeedbacks
                )
                payloadInterface.updatePayload(basicPayload)
                dismiss()
            }
        }
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }
}