package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.QopKirimlaridanQaytarilgan
import com.example.turon.databinding.ItemBagExpenseBinding

class BagExpenseAdapter(
    private var list: ArrayList<QopHistory>,
    var onpress: onPress
) :
    RecyclerView.Adapter<BagExpenseAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBagExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

            holder.bind(list[position], position, onpress)


    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemBagExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var bagType = binding.text3
        private var count = binding.text4
        private var providers = binding.text6
        private var date = binding.text5
        private var comment = binding.text7
        private var container = binding.container
        fun bind(
            data: QopHistory,
            position: Int,
            onpress: onPress
        ) {
            numeric.text = (position + 1).toString()
            bagType.text = data.type.name
            count.text = data.quantity.toString()
            providers.text = data.client.compony
            date.text = data.date
            comment.text = data.izoh ?: ""
            container.setOnClickListener {
                onpress.click(data, position)
            }
        }

    }

    interface onPress {
        fun click(data: QopHistory, position: Int)
    }
}