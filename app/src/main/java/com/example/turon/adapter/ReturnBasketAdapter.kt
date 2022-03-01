package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.ReturnBasket
import com.example.turon.data.model.ReturnedBasked
import com.example.turon.databinding.ItemOrderBaskedBinding
import com.example.turon.databinding.ItemSendProductBinding

class ReturnBasketAdapter(
    private var list: ArrayList<ReturnBasket>
) : RecyclerView.Adapter<ReturnBasketAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderBaskedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class VH(val binding: ItemOrderBaskedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: ReturnBasket
        ) {
            with(binding){
                count.text=data.hajmi.toString()
                product.text=data.maxsulot
            }
        }
    }


}