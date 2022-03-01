package com.example.turon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.ReturnedBasked
import com.example.turon.data.model.ReturnedSec
import com.example.turon.databinding.ItemSendProductBinding

class ReturnedSecurityAdapter   (
    private var list: ArrayList<ReturnedSec>,
    private var onReturnBaskedClickListener: OnReturnBaskedClickListener
) :
    RecyclerView.Adapter<ReturnedSecurityAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSendProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position], onReturnBaskedClickListener, position)
    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemSendProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var product = binding.text3
        private var company = binding.text4
        private var bagsCount = binding.text6
        private var date = binding.text5
        var rootLayout = binding.rootLayout
        fun bind(
            data: ReturnedSec,
            onReturnBaskedClickListener: OnReturnBaskedClickListener,
            position: Int
        ) {
            numeric.text = (position + 1).toString()
            product.text = data.product
            date.text = data.sana
            company.text = data.tegirmon
            bagsCount.text = data.qopsoni.toString()

            rootLayout.setOnClickListener {
                onReturnBaskedClickListener.onItemClickReturnBasked(data)
            }

        }
    }

    interface OnReturnBaskedClickListener {
        fun onItemClickReturnBasked(position: ReturnedSec)

    }

}