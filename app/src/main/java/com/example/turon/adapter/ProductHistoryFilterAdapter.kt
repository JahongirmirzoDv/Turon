package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.HistoryProData
import com.example.turon.databinding.ItemSendProductBinding

class ProductHistoryFilterAdapter() :
    RecyclerView.Adapter<ProductHistoryFilterAdapter.Vh>() {
    var lists: List<HistoryProData> = emptyList()

    constructor(lists: List<HistoryProData>) : this() {
        this.lists = lists
    }

    inner class Vh(var itemview: ItemSendProductBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(d: HistoryProData) {

            itemview.text1.text = (layoutPosition + 1).toString()
            itemview.text3.text = d.product.name
            itemview.text4.text = d.tegirmon.name
            itemview.text6.text = d.qopsoni.toString()
            itemview.text5.text = d.date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(parent.context)
        val listBinding = ItemSendProductBinding.inflate(v, parent, false)
        return Vh(listBinding)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = 1


}