package com.example.turon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.R
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.databinding.ItemHistoryBinding

class HistoryAdapter(
    private var onHistoryListener: OnHistoryListener,
    private var context: Context,
    private var list: ArrayList<ProductAcceptData>
) :
    RecyclerView.Adapter<HistoryAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(onHistoryListener, context, list[position],position)
    }

    override fun getItemCount(): Int = list.size

    class VH(binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var actionBtn = binding.cons
        private var imageView = binding.imageView3
        private var childView = binding.childView
        private var button = binding.button
        private var clientName = binding.textView2
        private var aktName = binding.textView6
        private var stansiya = binding.textView7
        private var wagonCount = binding.textView8
        private var editIcon = binding.imageView6
        private var numericTxt = binding.numericTxt
        private var date = binding.textView32

        fun bind(
            onProductAcceptListener: OnHistoryListener,
            context: Context,
            productAcceptData: ProductAcceptData,
            position: Int
        ) {
            if (productAcceptData.isEdited) editIcon.visibility = View.VISIBLE
            else editIcon.visibility = View.GONE

            clientName.text = productAcceptData.client.compony?:""
            aktName.text = productAcceptData.name?:""
            stansiya.text = productAcceptData.stansiya?:""
            wagonCount.text = productAcceptData.vagonSoni.toString()?:""
            date.text = productAcceptData.dateStart?:""
            numericTxt.text= (position+1).toString()
            actionBtn.setOnClickListener {
                if (childView.visibility == View.VISIBLE) {
                    childView.visibility = View.GONE
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic__down_bottom
                        )
                    )

                    clientName.setTextColor(Color.parseColor("#FF000000"))
                } else {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic__down_up
                        )
                    )
                    childView.visibility = View.VISIBLE
                    clientName.setTextColor(Color.parseColor("#FFAC33"))
                }
            }
            button.setOnClickListener {
                onProductAcceptListener.onItemHistoryListener(adapterPosition, productAcceptData)
            }
        }
    }

    interface OnHistoryListener {
        fun onItemHistoryListener(position: Int, productAcceptData: ProductAcceptData)

    }

}