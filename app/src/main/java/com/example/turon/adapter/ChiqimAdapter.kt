package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.BagExpenseHistory
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.QopChiqim
import com.example.turon.data.model.QopHistory
import com.example.turon.databinding.ItemBagExpenseBinding
import com.example.turon.databinding.ItemBagIncomeBinding
import com.example.turon.databinding.ItemSendProductBinding
import com.example.turon.databinding.QopChiqimBinding

class ChiqimAdapter():RecyclerView.Adapter<ChiqimAdapter.VH>() {
    var list: List<QopChiqim> = emptyList()

    constructor(lists: List<QopChiqim>) : this() {
        this.list = lists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = QopChiqimBinding.inflate(
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

    class VH(binding: QopChiqimBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var bagType = binding.text3
        private var count = binding.text4
        private var providers = binding.text6
        private var date = binding.text5
        fun bind(
            data: QopChiqim,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            bagType.text = data.type.name
            count.text = data.quantity.toString()
            providers.text = data.date
            date.text = data.izoh
        }
    }

}