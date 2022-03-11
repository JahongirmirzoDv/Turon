package com.example.turon.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.App
import com.example.turon.data.model.ActiveTurn
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.response.Activetashkent
import com.example.turon.databinding.ItemSendProductBinding
import com.example.turon.utils.SharedPref

class TurnHistoryAdapter(
    private var list: ArrayList<Activetashkent>
) :
    RecyclerView.Adapter<TurnHistoryAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSendProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun deleteItem(position: Int){
        try {
            list.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: java.lang.Exception) {
            Log.e("MainActivity", e.message!!)
        }


    }



    class VH(binding: ItemSendProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private var numeric = binding.text1
        private var client = binding.text3
        private var carNum = binding.text4
        private var phoneNum = binding.text6
        private var date = binding.text5
        var rootLayout = binding.rootLayout
        fun bind(data: Activetashkent) {
            numeric.text = (position + 1).toString()
            client.text = data.mijoz
            date.text = data.date
            carNum.text = data.carNumber
            phoneNum.text = data.driverPhone



        }


    }


    interface OnHistoryClickListener {
        fun onItemClickOrder(position: Activetashkent)

    }

}