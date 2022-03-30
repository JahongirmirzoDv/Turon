package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.ResultN
import com.example.turon.databinding.YuborishTarixItemBinding

class SendOrderHistoryAdapter :
    PagingDataAdapter<ResultN, SendOrderHistoryAdapter.ViewHolder>(CDiffUtilCallback()) {
    private lateinit var onParcelClickListener: OnParcelClickListener
    var state: Boolean = true

//    qoplar kirim
//    ichkaridagilar
//    navbat kutayotganlar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listBinding = YuborishTarixItemBinding.inflate(v, parent, false)
        return ViewHolder(listBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, position)
    }

    inner class ViewHolder(val binding: YuborishTarixItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(d: ResultN, position: Int) {
            var string = ""
            try {
                with(binding) {
                    root.setOnClickListener {
                        onParcelClickListener.clickListener(getItem(position)!!)
                    }
                    text1.text = (layoutPosition + 1).toString().ifEmpty { "" }
                    text3.text = d.customer.name.ifEmpty { "" }
                    text4.text = d.car_number.ifEmpty { "" }
                    text6.text = d.driver_phone.ifEmpty { "" }
                    text5.text = d.date_time.ifEmpty { "" }
                    chiqish.text = if (d.left_date != "") d.left_date
                        .substring(0, 19) else "malumot yo'q"
                    d.baskets.forEach {
                        when (it.product.product.type.id) {
                            1 -> string = "U"
                            2 -> string = if (string.isNotEmpty()) "U+Y" else "Y"
                            else -> {
                                ""
                            }
                        }
                    }
                    state.text = string.ifEmpty { "" }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setOnClickListener(listener: OnParcelClickListener) {
        this.onParcelClickListener = listener
    }

    @JvmName("setStatus1")
    fun setStatus(stat: Boolean) {
        this.state = stat
    }

    interface OnParcelClickListener {
        fun clickListener(parcel: ResultN)
    }
}

class CDiffUtilCallback : DiffUtil.ItemCallback<ResultN>() {
    override fun areItemsTheSame(oldItem: ResultN, newItem: ResultN): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultN, newItem: ResultN): Boolean {
        return oldItem.id == newItem.id
    }
}