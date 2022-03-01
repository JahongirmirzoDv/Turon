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
import com.example.turon.data.model.ProductPro
import com.example.turon.data.model.SpinnerForProduct

class ProductProAdapter (context: Context, list: List<ProductPro>) :
    ArrayAdapter<ProductPro>(context, 0, list) {
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
            views.findViewById<TextView>(R.id.companyName).text=data.name
            views.findViewById<TextView>(R.id.companyName).gravity= Gravity.CENTER
        }

        return views
    }
}