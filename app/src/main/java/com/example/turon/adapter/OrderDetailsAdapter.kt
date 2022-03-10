package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.Balance
import com.example.turon.databinding.ItemSendDetailsBinding

class OrderDetailsAdapter(
    private var list: ArrayList<Balance>,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<OrderDetailsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSendDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position], onOrderClickListener, position)
    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemSendDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var productName = binding.text3
        private var countBags = binding.text4
        private var company = binding.text6
        var rootLayout = binding.tabsLayout

        @SuppressLint("SetTextI18n")
        fun bind(
            data: Balance,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
            try {
                numeric.text = (position + 1).toString()
                productName.text = data.productName
                countBags.text = data.bagCount.toString()
                company.text = data.company

                rootLayout.setOnClickListener {
                    onOrderClickListener.onItemClickOrderDetails(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    interface OnOrderClickListener {
        fun onItemClickOrderDetails(data: Balance)

    }

}