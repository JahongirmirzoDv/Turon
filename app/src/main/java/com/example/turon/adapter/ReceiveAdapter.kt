package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.databinding.ReceiveItemBinding

class ReceiveAdapter : RecyclerView.Adapter<ReceiveAdapter.Vh>() {
//    var list: List<Detail> = emptyList()

    inner class Vh(val itemview: ReceiveItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ReceiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
//        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int  =0//= list.size
}