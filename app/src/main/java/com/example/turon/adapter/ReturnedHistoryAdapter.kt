package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.ResultN
import com.example.turon.databinding.ItemReturnProductBinding

class ReturnedHistoryAdapter :
    PagingDataAdapter<ResultN, ReturnedHistoryAdapter.ViewHolder>(FDiffUtilCallback()) {
    private lateinit var onParcelClickListener: OnParcelClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listBinding = ItemReturnProductBinding.inflate(v, parent, false)
        return ViewHolder(listBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }


    inner class ViewHolder(val binding: ItemReturnProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(d: ResultN) {
            with(binding) {
                text1.text = (layoutPosition + 1).toString()
                text3.text = d.customer.name
                text6.text = d.customer.phone
                text5.text = d.date
                root.setOnClickListener {
                    onParcelClickListener.clickListener(d)
                }
            }
        }
    }

    fun setOnClickListener(listener: OnParcelClickListener) {
        this.onParcelClickListener = listener
    }

    interface OnParcelClickListener {
        fun clickListener(parcel: ResultN)
    }

}

class FDiffUtilCallback : DiffUtil.ItemCallback<ResultN>() {
    override fun areItemsTheSame(oldItem: ResultN, newItem: ResultN): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultN, newItem: ResultN): Boolean {
        return oldItem.id == newItem.id
    }
}