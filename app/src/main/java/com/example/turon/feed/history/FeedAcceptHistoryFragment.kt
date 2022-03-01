package com.example.turon.feed.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.AcceptanceHistoryAdapter
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.ProductionHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Company
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.data.model.factory.FeedAcceptanceHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.DialogItemBinding
import com.example.turon.databinding.FragmentFeedAcceptHistoryBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class FeedAcceptHistoryFragment : Fragment(){
    private var _binding: FragmentFeedAcceptHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyAdapter by lazy { AcceptanceHistoryAdapter() }
    private val historyList by lazy { ArrayList<HistoryProData>() }
    private lateinit var progressDialog: AlertDialog

    private val viewModel: AllHistoryViewModel by viewModels{
        AllHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java,requireContext())
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedAcceptHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }


    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        setRecycler()
        getOrderHistory()
    }


    private fun setRecycler() {
        binding.recyclerHistory.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=historyAdapter.withLoadStateHeaderAndFooter(
                header= AdvertLoadStateAdapter{historyAdapter.retry()},
                footer= AdvertLoadStateAdapter{historyAdapter.retry()}
            )
        }

        historyAdapter.addLoadStateListener {loadStates ->

            when (loadStates.refresh) {
                is LoadState.NotLoading -> {
                    progressDialog.dismiss()
                }
                is LoadState.Loading -> {
                    progressDialog.show()
                }
                is LoadState.Error -> {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun getOrderHistory() {
        val userId= SharedPref(requireContext()).getUserId()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response=viewModel.getAcceptancePagination(userId)
            response.collect {
                historyAdapter.submitData(it)
            }
        }

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