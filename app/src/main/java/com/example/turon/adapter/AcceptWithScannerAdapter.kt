package com.example.turon.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.AcceptProductScanner
import com.example.turon.databinding.ItemScannerLayoutBinding

class AcceptWithScannerAdapter(private var onScanListener: OnScanListenerT) :
    ListAdapter<AcceptProductScanner, AcceptWithScannerAdapter.VH>(DiffCallbackScan()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemScannerLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data: AcceptProductScanner = getItem(position)
        holder.bind(data, position, onScanListener)

    }

    inner class VH(binding: ItemScannerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private var ID = binding.text1
        private var productName = binding.text2
        private var productCode = binding.text3
        private var rootLayout = binding.rootLayout
        private var checkBox = binding.childCheckbox

        @SuppressLint("SetTextI18n")
        fun bind(
            data: AcceptProductScanner,
            position: Int,
            onScanListener: OnScanListenerT
        ) {
            ID.text = (position + 1).toString()
            productName.text = data.productName
            productCode.text = data.crCode.toString()


        }



    }

}

interface OnScanListenerT {
    fun onItemScanListener(isLong: Boolean)

}

class DiffCallbackScan : DiffUtil.ItemCallback<AcceptProductScanner>() {
    override fun areItemsTheSame(
        oldItem: AcceptProductScanner,
        newItem: AcceptProductScanner
    ): Boolean {
        return oldItem.crCode == newItem.crCode
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: AcceptProductScanner,
        newItem: AcceptProductScanner
    ): Boolean {
        return oldItem == newItem
    }

}