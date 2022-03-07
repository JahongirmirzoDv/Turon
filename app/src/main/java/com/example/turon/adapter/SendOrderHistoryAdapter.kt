package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.OrderHistory
import com.example.turon.databinding.YuborishTarixItemBinding

class SendOrderHistoryAdapter :
    PagingDataAdapter<OrderHistory, SendOrderHistoryAdapter.ViewHolder>(CDiffUtilCallback()) {
    private lateinit var onParcelClickListener: OnParcelClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listBinding =
            YuborishTarixItemBinding.inflate(v, parent, false)
        return ViewHolder(listBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    inner class ViewHolder(val binding: YuborishTarixItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(d: OrderHistory) {
           try {
               with(binding) {
                   text1.text = (layoutPosition + 1).toString()
                   text3.text = d.customer.name
                   text4.text = d.car_number
                   text6.text = d.driver_phone
                   text5.text = d.date
                   chiqish.text = if (d.left_date != "") d.left_date.toString()
                       .substring(0, 19) else "malumot yo'q"
                   root.setOnClickListener {
                       onParcelClickListener.clickListener(d)
                   }
               }
           }catch (e:Exception){
               e.printStackTrace()
           }
        }
    }

    fun setOnClickListener(listener: OnParcelClickListener) {
        this.onParcelClickListener = listener
    }

    interface OnParcelClickListener {
        fun clickListener(parcel: OrderHistory)
    }

}

class CDiffUtilCallback : DiffUtil.ItemCallback<OrderHistory>() {
    override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
        return oldItem.id == newItem.id
    }
}