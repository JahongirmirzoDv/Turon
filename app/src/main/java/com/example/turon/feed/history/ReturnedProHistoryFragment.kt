package com.example.turon.feed.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.AcceptanceHistoryAdapter
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentReturnedProHistoryBinding
import com.example.turon.databinding.FragmentReturnedProductionBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect

class ReturnedProHistoryFragment : Fragment() {
private var _binding:FragmentReturnedProHistoryBinding?=null
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
        _binding= FragmentReturnedProHistoryBinding.inflate(inflater,container,false)
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
        binding.recyclerHistoryPro.apply {
            layoutManager= LinearLayoutManager(requireContext())
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response=viewModel.getReturnedProPagination()
            response.collect {
                historyAdapter.submitData(it)
            }
        }

    }


}