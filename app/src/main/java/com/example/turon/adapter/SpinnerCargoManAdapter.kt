package com.example.turon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.turon.R
import com.example.turon.data.model.Company
import com.example.turon.data.model.response.ProductData
import com.example.turon.data.model.response.TegirmonData

class SpinnerCargoManAdapter(context: Context, list: List<TegirmonData>) :
    ArrayAdapter<TegirmonData>(context, 0, list) {
    val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)

    }

    @SuppressLint("CutPasteId")
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val company = getItem(position)
        val views = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        if (company != null) {
            views.findViewById<TextView>(R.id.companyName).text=company.name
            views.findViewById<TextView>(R.id.companyName).gravity=Gravity.START
        }
        return views
    }
}