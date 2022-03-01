package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.BrCode
import com.example.turon.data.model.CodeScan
import com.example.turon.databinding.ItemScannerLayoutBinding

class ReturnedProductListAdapter(private var onScanListener: OnOrderScanListenerF) :
    ListAdapter<CodeScan, ReturnedProductListAdapter.VH>(DiffCallbackScanListF()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemScannerLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data: CodeScan = getItem(position)
        holder.bind(data, position, onScanListener)

    }

    inner class VH(binding: ItemScannerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private var ID = binding.text1
        private var productName = binding.text2
        private var productCode = binding.text3

        @SuppressLint("SetTextI18n")
        fun bind(
            data: CodeScan,
            position: Int,
            onScanListener: OnOrderScanListenerF
        ) {
            ID.text = (position + 1).toString()
            productName.text = data.productName
            productCode.text = data.code.toString()



        }



    }

}

interface OnOrderScanListenerF {
    fun onItemScanListener(isLong: Boolean)

}

class DiffCallbackScanListF : DiffUtil.ItemCallback<CodeScan>() {
    override fun areItemsTheSame(
        oldItem: CodeScan,
        newItem: CodeScan
    ): Boolean {
        return oldItem.code == newItem.code
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: CodeScan,
        newItem: CodeScan
    ): Boolean {
        return oldItem == newItem
    }

}