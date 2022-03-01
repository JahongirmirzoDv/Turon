package com.example.turon.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.turon.security.ui.TurnAcceptFragment
import com.example.turon.security.ui.TurnHistoryFragment

class TurnPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val list = arrayListOf(TurnAcceptFragment(), TurnHistoryFragment())

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {

        return list[position]
    }


}