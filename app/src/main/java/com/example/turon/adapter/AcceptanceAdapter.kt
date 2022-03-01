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
import com.example.turon.databinding.ItemCommadityBinding

class AcceptanceAdapter(
    private var onProductAcceptListener: OnProductAcceptListener,
    private var context: Context,
    private var list: ArrayList<ProductAcceptData>
) :
    RecyclerView.Adapter<AcceptanceAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCommadityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(onProductAcceptListener, context, list[position],position)
    }

    override fun getItemCount(): Int =list.size

    class VH(binding: ItemCommadityBinding) : RecyclerView.ViewHolder(binding.root) {
        private var actionBtn = binding.cons
        private var imageView = binding.imageView3
        private var childView = binding.childView
        private var button = binding.button
        private var clientName = binding.textView2
        private var aktName = binding.textView6
        private var stansiya = binding.textView7
        private var wagonCount = binding.textView8
        private var numericTxt = binding.numericTxt
        fun bind(
            onProductAcceptListener: OnProductAcceptListener,
            context: Context,
            productAcceptData: ProductAcceptData,
            position: Int
        ) {
            clientName.text = productAcceptData.client.compony
            aktName.text = productAcceptData.name
            stansiya.text = productAcceptData.stansiya
            wagonCount.text = productAcceptData.vagonSoni.toString()
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
                onProductAcceptListener.onItemProductAcceptListener(adapterPosition,productAcceptData)
            }
        }
    }


    interface OnProductAcceptListener {
        fun onItemProductAcceptListener(position: Int,productAcceptData:ProductAcceptData)

    }

}