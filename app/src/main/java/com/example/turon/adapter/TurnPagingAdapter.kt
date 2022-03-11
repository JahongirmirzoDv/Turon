package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.TurnHistory
import com.example.turon.databinding.ItemSendProductBinding

class TurnPagingAdapter : PagingDataAdapter<TurnHistory, TurnPagingAdapter.ViewHolder>(EDiffUtilCallback()) {
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
        fun bind(d: TurnHistory) {
            with (binding) {
                text1.text=(layoutPosition+1).toString()
                text3.text= d.order?.customer?.name ?: ""
                text4.text= d.order?.car_number ?: ""
                text6.text= d.order?.driver_phone ?: ""
                text5.text= d.date
            }
            onParcelClickListener.clickListener(d)
        }
    }

    fun setOnClickListener(listener: OnParcelClickListener) {
        this.onParcelClickListener = listener
    }

    interface OnParcelClickListener {
        fun clickListener(parcel: TurnHistory)
    }

}

class EDiffUtilCallback : DiffUtil.ItemCallback<TurnHistory>() {
    override fun areItemsTheSame(oldItem: TurnHistory, newItem: TurnHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TurnHistory, newItem: TurnHistory): Boolean {
        return oldItem.id == newItem.id
    }
}