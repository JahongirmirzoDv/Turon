package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.response.OrderData
import com.example.turon.databinding.ItemReturnProductBinding

class ReturnProductAdapter (
    private var list: ArrayList<OrderData>,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<ReturnProductAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemReturnProductBinding.inflate(
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

    class VH(binding: ItemReturnProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var phoneNum = binding.text6
        private var date = binding.text5
        var rootLayout = binding.rootLayout
        fun bind(
            data: OrderData,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            client.text = data.client
            date.text = data.date
            phoneNum.text = data.phone

            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrder(data)
            }

        }
    }


    interface OnOrderClickListener {
        fun onItemClickOrder(position: OrderData)

    }

}