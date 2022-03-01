package com.example.turon.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.turon.production.ui.AcceptHistoryProFragment
import com.example.turon.production.ui.AcceptanceProFragment


class ProPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val list = arrayListOf(AcceptanceProFragment(), AcceptHistoryProFragment())

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {

        return list[position]
    }


}