package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.data.model.EditScales
import com.example.turon.data.model.response.AcceptDetailsWagon
import com.example.turon.databinding.ItemTableBinding
import com.example.turon.scales.ui.histroy.ScalesHistoryDetailsFragment
import com.example.turon.utils.textChanges
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductAdapter(private var onEditListener: ScalesHistoryDetailsFragment) :
    ListAdapter<AcceptDetailsWagon, ProductAdapter.VH>(DiffCallback()) {
    //var selectionModeOn = false


    fun selectionMode(position: Int, brutto: Int, tara: Int) {
        currentList[position].bruttoFakt = brutto
        currentList[position].bruttoFakt = tara
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val data: AcceptDetailsWagon = getItem(position)
        holder.bind(data, position, onEditListener)
    }


    class VH(binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {
        private var ID = binding.num
        private var tara = binding.tara
        private var brutto = binding.brutto
        private var wagonNum = binding.wagonNum

        @SuppressLint("SetTextI18n")
        fun bind(data: AcceptDetailsWagon, position: Int, onEditListener: OnEditListenerT) {
            ID.text = (position + 1).toString()
            tara.setText(data.taraFakt.toString())
            brutto.setText(data.bruttoFakt.toString())
            wagonNum.text = data.number.toString()
            val editScales = EditScales()
            editScales.wagon_id = data.id
            editScales.brutto = data.bruttoFakt
            editScales.tara = data.taraFakt


            tara.onFocusChangeListener = View.OnFocusChangeListener { _, state ->
                if (state) {
                    tara.setText("")
                }
                else {
                    tara.setText(editScales.tara.toString())
                }
                CoroutineScope(context = Dispatchers.Main).launch {
                    val editText = tara.textChanges()
                    editText
                        .debounce(500)
                        .onEach {
                            if (data.taraFakt.toString() != tara.text.toString() && !tara.text.isNullOrEmpty()) {
                                editScales.tara = tara.text.toString().toInt()
                                onEditListener.onItemEditListener(
                                    layoutPosition,
                                    data.id,
                                    editScales.brutto,
                                    tara.text.toString().toInt(),
                                )
                            }
                        }
                        .launchIn(this)
                }
            }

            brutto.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
                if (b) {
                    brutto.setText("")
                } else {
                    brutto.setText(editScales.brutto.toString())
                }
                CoroutineScope(context = Dispatchers.Main).launch {
                    val editTextFlow = brutto.textChanges()
                    editTextFlow
                        .debounce(500)
                        .onEach {
                            if (data.bruttoFakt.toString() != brutto.text.toString() && !brutto.text.isNullOrEmpty()) {
                                editScales.brutto = brutto.text.toString().toInt()

                                onEditListener.onItemEditListener(
                                    layoutPosition,
                                    data.id,
                                    brutto.text.toString().toInt(),
                                    editScales.tara,
                                )
                            }
                        }
                        .launchIn(this)
                }
            }
        }
    }
}

interface OnEditListenerT {
    fun onItemEditListener(
        position: Int,
        wagonId: Int,
        brutto: Int,
        tara: Int,
    )
}

class DiffCallback : DiffUtil.ItemCallback<AcceptDetailsWagon>() {
    override fun areItemsTheSame(
        oldItem: AcceptDetailsWagon,
        newItem: AcceptDetailsWagon
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AcceptDetailsWagon,
        newItem: AcceptDetailsWagon
    ): Boolean {
        return oldItem == newItem
    }
}