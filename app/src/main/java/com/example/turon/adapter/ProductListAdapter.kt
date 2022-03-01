package com.example.turon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.R
import com.example.turon.data.model.AcceptProduct
import com.example.turon.data.model.response.AcceptDetailsWagon
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.databinding.ItemTableBinding
import com.example.turon.databinding.OrderLayoutBinding
import com.example.turon.utils.textChanges
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductListAdapter(
    private var onEditListener: OnEditListener,
    private var list: ArrayList<AcceptDetailsWagon>
) :
    RecyclerView.Adapter<ProductListAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(onEditListener, list[position], position)
    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {
        private var ID = binding.num
        private var tara = binding.tara
        private var brutto = binding.brutto
        private var wagonNum = binding.wagonNum

        @SuppressLint("SetTextI18n")
        fun bind(onEditListener: OnEditListener, data: AcceptDetailsWagon, position: Int) {
            ID.text = (position + 1).toString()
            tara.setText(data.taraFakt.toString())
            brutto.setText(data.bruttoFakt.toString())
            wagonNum.text = data.number.toString()


            CoroutineScope(context = Dispatchers.Main).launch {
                val editTextFlow = brutto.textChanges()
                editTextFlow
                    .debounce(2000)
                    .onEach {
                        if (data.bruttoFakt.toString()!=brutto.text.toString()){
                            onEditListener.onItemEditListener(layoutPosition, data.id,brutto.text.toString(),data.taraFakt.toString())
                        }
                    }
                    .launchIn(this)



            }

            CoroutineScope(context = Dispatchers.Main).launch {
                val editText = tara.textChanges()
                editText
                    .debounce(2000)
                    .onEach {
                       if (data.taraFakt.toString()!=tara.text.toString()){
                           onEditListener.onItemEditListener(adapterPosition, data.id,data.bruttoFakt.toString(),tara.text.toString())
                       }

                    }
                    .launchIn(this)
            }

        }
    }


    interface OnEditListener {
        fun onItemEditListener(position: Int, wagonId:Int,brutto:String,tara:String)

    }
}