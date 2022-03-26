package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.Qoblar
import com.example.turon.databinding.QoldiqItemBinding

class Qoldiq(
    private var list: ArrayList<Qoblar>
) : RecyclerView.Adapter<Qoldiq.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = QoldiqItemBinding.inflate(
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

    class VH(binding: QoldiqItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var bagType = binding.text3
        private var count = binding.text4
        private var comment = binding.text7
        fun bind(
            data: Qoblar,
            position: Int
        ) {
            try {
                bagType.text = data.type.name
                count.text = data.quantity.toString()
                comment.text = data.tegirmon.name
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}