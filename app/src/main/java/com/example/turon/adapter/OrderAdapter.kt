package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.App
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.response.OrderData
import com.example.turon.databinding.ItemSendProductBinding
import com.example.turon.utils.SharedPref

class OrderAdapter(
    private var list: ArrayList<OrderData>,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<OrderAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSendProductBinding.inflate(
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

    class VH(binding: ItemSendProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var carNum = binding.text4
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
            carNum.text = data.carNum
            phoneNum.text = data.phone ?: ""

            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrder(data)
            }

        }
    }


    interface OnOrderClickListener {
        fun onItemClickOrder(position: OrderData)

    }

}