package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.Turn
import com.example.turon.databinding.ItemTurnAcceptBinding

class TurnAdapter(
    private var list: ArrayList<Turn>,
    private var onOrderClickListener: OnOrderClickListener
) :
    RecyclerView.Adapter<TurnAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTurnAcceptBinding.inflate(
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

    class VH(binding: ItemTurnAcceptBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var date = binding.text5
        private var rootLayout = binding.tabsLayout
        fun bind(
            data: Turn,
            onOrderClickListener: OnOrderClickListener,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            client.text = data.mijoz
            date.text = data.sana

            rootLayout.setOnClickListener {
                onOrderClickListener.onItemClickOrder(data)
            }

        }
    }


    interface OnOrderClickListener {
        fun onItemClickOrder(data: Turn)

    }

}