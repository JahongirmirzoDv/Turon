package com.example.turon.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.App
import com.example.turon.data.model.EditScales
import com.example.turon.data.model.Tarozi
import com.example.turon.data.model.response.AcceptDetailsWagon
import com.example.turon.databinding.ItemTableBinding
import com.example.turon.scales.ui.histroy.ScalesHistoryDetailsFragment
import com.example.turon.utils.SharedPref
import com.example.turon.utils.textChanges
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            val sharedPref by lazy { SharedPref(App.instance) }

            val gson = Gson()

            val tara_list = ArrayList<Tarozi>()
            val brutto_list = ArrayList<Tarozi>()

            tara.setText(data.taraFakt.toString())
            brutto.setText(data.bruttoFakt.toString())

            ID.text = (position + 1).toString()

            val type = object : TypeToken<List<Tarozi>>() {}.type

            if (sharedPref.tara != "") {
                Log.e("TAG", "tara: ${sharedPref.tara}")
                var g1 = gson.fromJson<List<Tarozi>>(sharedPref.tara, type)
                tara_list.addAll(g1)
            }


            if (sharedPref.brutto != "") {
                Log.e("TAG", "bind: ${sharedPref.brutto}")
                var g2 = gson.fromJson<List<Tarozi>>(sharedPref.brutto, type)
                brutto_list.addAll(g2)
            }

            if (tara_list.isNotEmpty()) {
                tara_list.forEach {
                    if (data.id == it.id) {
                        tara.setText(it.w)
                    }
                }
            } else tara.setText(data.taraFakt.toString())

            if (brutto_list.isNotEmpty()) {
                brutto_list.forEach {
                    if (data.id == it.id) {
                        brutto.setText(it.w)
                    }
                }
            } else brutto.setText(data.bruttoFakt.toString())

            Log.e("TAG", "tara: ${sharedPref.tara}")
            Log.e("TAG", "brutto: ${sharedPref.brutto}")


            wagonNum.text = data.number.toString()
            val editScales = EditScales()
            editScales.wagon_id = data.id
            editScales.brutto = data.bruttoFakt
            editScales.tara = data.taraFakt


            tara.onFocusChangeListener = View.OnFocusChangeListener { _, state ->
//                if (state) {
//                    tara.setText("")
//                } else {
//                    tara.setText(editScales.tara.toString())
//                }
                CoroutineScope(context = Dispatchers.Main).launch {
                    val editText = tara.textChanges()
                    editText
                        .debounce(500)
                        .onEach {
                            if (data.taraFakt.toString() != tara.text.toString() && !tara.text.isNullOrEmpty()) {
                                editScales.tara = tara.text.toString().toInt()
                                tara_list.add(Tarozi(tara.text.toString(), data.id))
                                sharedPref.tara = gson.toJson(tara_list)

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
//                if (b) {
//                    brutto.setText("")
//                } else {
//                    brutto.setText(editScales.brutto.toString())
//                }
                CoroutineScope(context = Dispatchers.Main).launch {
                    val editTextFlow = brutto.textChanges()
                    editTextFlow
                        .debounce(500)
                        .onEach {
                            if (data.bruttoFakt.toString() != brutto.text.toString() && !brutto.text.isNullOrEmpty()) {
                                editScales.brutto = brutto.text.toString().toInt()
                                brutto_list.add(Tarozi(brutto.text.toString(), data.id))
                                sharedPref.brutto = gson.toJson(brutto_list)

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
        newItem: AcceptDetailsWagon,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AcceptDetailsWagon,
        newItem: AcceptDetailsWagon,
    ): Boolean {
        return oldItem == newItem
    }
}