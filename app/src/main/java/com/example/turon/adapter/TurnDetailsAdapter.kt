package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.response.OrderDetailsData
import com.example.turon.databinding.ItemSendDetailsBinding

class TurnDetailsAdapter (
    private var list: ArrayList<ClientData>,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<TurnDetailsAdapter.VH>() {

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
        private var massa = binding.text6
        var rootLayout = binding.tabsLayout
        @SuppressLint("SetTextI18n")
        fun bind(
            data: ClientData,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
            numeric.text=(position+1).toString()
            productName.text = data.maxsulot
            countBags.text = data.qop_soni.toString()
            massa.text = data.umumiy_massa.toString()

            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrderDetails(data)
            }

        }
    }


    interface OnOrderClickListener {
        fun onItemClickOrderDetails(data: ClientData)

    }

}