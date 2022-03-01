package com.example.turon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.turon.R
import com.example.turon.data.model.LoadOrder

class OrderBasketSpinner(context: Context, list: List<LoadOrder>) :
    ArrayAdapter<LoadOrder>(context, 0, list) {
    val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)

    }

    @SuppressLint("CutPasteId")
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItem(position)
        val views = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        if (data != null) {
            if (data.status){
                views.findViewById<TextView>(R.id.companyName).gravity= Gravity.CENTER
                views.findViewById<TextView>(R.id.companyName).text=data.product
                views.findViewById<TextView>(R.id.companyName).setTextColor(Color.parseColor("#ABABAB"))
            }else{
                views.findViewById<TextView>(R.id.companyName).gravity= Gravity.CENTER
                views.findViewById<TextView>(R.id.companyName).text=data.product
                views.findViewById<TextView>(R.id.companyName).setTextColor(Color.parseColor("#000000"))
            }

        }

        return views
    }
}