package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.AcceptProductScanner
import com.example.turon.data.model.BrCode
import com.example.turon.data.model.CodeScan
import com.example.turon.databinding.ItemScannerLayoutBinding

class OrderListAdapter(private var onScanListener: OnOrderScanListenerT) :
ListAdapter<BrCode, OrderListAdapter.VH>(DiffCallbackScanList()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemScannerLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data: BrCode = getItem(position)
        holder.bind(data, position, onScanListener)

    }

    inner class VH(binding: ItemScannerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private var ID = binding.text1
        private var productName = binding.text2
        private var productCode = binding.text3

        @SuppressLint("SetTextI18n")
        fun bind(
            data: BrCode,
            position: Int,
            onScanListener: OnOrderScanListenerT
        ) {
            ID.text = (position + 1).toString()
            productName.text = data.productName
            productCode.text = data.code.toString()



        }



    }

}

interface OnOrderScanListenerT {
    fun onItemScanListener(isLong: Boolean)

}

class DiffCallbackScanList : DiffUtil.ItemCallback<BrCode>() {
    override fun areItemsTheSame(
        oldItem: BrCode,
        newItem: BrCode
    ): Boolean {
        return oldItem.code == newItem.code
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: BrCode,
        newItem: BrCode
    ): Boolean {
        return oldItem == newItem
    }

}