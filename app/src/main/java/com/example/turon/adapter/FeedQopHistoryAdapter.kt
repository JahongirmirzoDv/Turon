package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.FeedQopChiqimHistory
import com.example.turon.data.model.QopChiqim
import com.example.turon.databinding.FeedQopChiqimHistoryItemBinding
import com.example.turon.databinding.ItemSendProductBinding

class FeedQopHistoryAdapter(var list: List<QopChiqim>) :
    RecyclerView.Adapter<FeedQopHistoryAdapter.Vh>() {


    inner class Vh(var itemview: FeedQopChiqimHistoryItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(d: QopChiqim) {

            itemview.text1.text = d.type.name
            itemview.text3.text = d.date
            itemview.text4.text = d.quantity.toString()
            itemview.text6.text = d.tegirmon.toString()
            itemview.text5.text = d.izoh

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(parent.context)
        val listBinding = FeedQopChiqimHistoryItemBinding.inflate(v, parent, false)
        return Vh(listBinding)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}