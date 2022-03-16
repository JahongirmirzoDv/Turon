package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.R
import com.example.turon.data.model.response.OrderData
import com.example.turon.databinding.ItemSendProductBinding
import com.example.turon.databinding.WithStatusBinding

class OrderAdapter(
    private var list: ArrayList<OrderData>,
    private var user: String,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isTrue: Boolean = false

    fun setT(ss: Boolean) {
        this.isTrue = ss
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 2) {
            return VH(
                WithStatusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return VHN(
                ItemSendProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 2) {
            val fromVh = holder as VH
            fromVh.bind(list[position], onOrderClickListener, position)
        } else {
            val toVh = holder as VHN
            toVh.bind(list[position], onOrderClickListener, position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class VH(binding: WithStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var carNum = binding.text4
        private var phoneNum = binding.text6
        private var date = binding.text5
        private var status = binding.status
        var rootLayout = binding.rootLayout

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(
            data: OrderData,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
                when (data.status) {
                    "2", "3" -> {
                        status.setBackgroundResource(R.color.yellow)
                    }
                    "1" -> {
                        status.setBackgroundResource(R.color.qizil)
                    }
//                "5", "4" -> {
//                    status.setBackgroundResource(R.color.qizil)
//                }
                }

            numeric.text = (position + 1).toString()
            client.text = data.client
            date.text = data.date
            carNum.text = data.carNum
            phoneNum.text = data.phone
            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrder(data)
            }
        }
    }

    inner class VHN(binding: ItemSendProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var carNum = binding.text4
        private var phoneNum = binding.text6
        private var date = binding.text5
        var rootLayout = binding.rootLayout

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(
            data: OrderData,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            client.text = data.client
            date.text = data.date
            carNum.text = data.carNum
            phoneNum.text = data.phone
            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrder(data)
            }
        }
    }

    interface OnOrderClickListener {
        fun onItemClickOrder(position: OrderData)

    }

    override fun getItemViewType(position: Int): Int {
        return if (isTrue) {
            1
        } else 2
    }
}