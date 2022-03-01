package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.HistoryProData
import com.example.turon.databinding.ItemSendProductBinding

class AcceptanceHistoryAdapter  : PagingDataAdapter<HistoryProData, AcceptanceHistoryAdapter.ViewHolder>(ADiffUtilCallback()) {
    private lateinit var onParcelClickListener: OnParcelClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listBinding = ItemSendProductBinding.inflate(v, parent, false)
        return ViewHolder(listBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }


    inner class ViewHolder(val binding: ItemSendProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(d: HistoryProData) {
            with (binding) {
                text1.text=(layoutPosition+1).toString()
                text3.text=d.product.name
                text4.text=d.tegirmon.name
                text6.text=d.qopsoni.toString()
                text5.text=d.date
            }
        }
    }

    fun setOnClickListener(listener: OnParcelClickListener) {
        this.onParcelClickListener = listener
    }

    interface OnParcelClickListener {
        fun clickListener(parcel: HistoryProData)
    }

}

class ADiffUtilCallback : DiffUtil.ItemCallback<HistoryProData>() {
    override fun areItemsTheSame(oldItem: HistoryProData, newItem: HistoryProData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoryProData, newItem: HistoryProData): Boolean {
        return oldItem.id == newItem.id
    }
}