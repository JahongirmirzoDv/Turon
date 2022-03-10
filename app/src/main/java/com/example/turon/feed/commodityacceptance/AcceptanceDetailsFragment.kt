package com.example.turon.feed.commodityacceptance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.ProductListAdapter
import com.example.turon.data.model.AcceptProduct
import com.example.turon.databinding.FragmentAcceptanceDetailsBinding


class AcceptanceDetailsFragment : Fragment() {
    private var _binding: FragmentAcceptanceDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductListAdapter
    private lateinit var list: ArrayList<AcceptProduct>
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcceptanceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAction()

        setupUi()

    }

    private fun setupUi() {

    }

    private fun initAction() {
        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.newAccept -> {
                        findNavController().navigate(R.id.commodityAcceptanceFragment2)
                        true
                    }
                    R.id.history -> {
                        findNavController().navigate(R.id.feedAcceptHistoryFragment)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_feed_accept)
            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.d("TAG", "Error show menu icon")
            } finally {
                popupMenu.show()
            }
        }
    }
}