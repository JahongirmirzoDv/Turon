package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.BagExpenseHistory
import com.example.turon.databinding.ItemBagIncomeBinding

class BagInComeAdapter(
    private var list: ArrayList<BagExpenseHistory>) : RecyclerView.Adapter<BagInComeAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBagIncomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemBagIncomeBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var bagType = binding.text3
        private var count = binding.text4
        private var date = binding.text5
        private var comment = binding.text7
        fun bind(
            data: BagExpenseHistory,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            bagType.text = data.type.name
            count.text = data.quantity.toString()
            date.text = data.date
            comment.text = data.izoh ?: ""
        }
    }

}