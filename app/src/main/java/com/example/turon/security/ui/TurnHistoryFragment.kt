package com.example.turon.security.ui

import android.app.AlertDialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.ProductionHistoryAdapter
import com.example.turon.adapter.TurnPagingAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.data.model.factory.TurnAcceptViewModelFactory
import com.example.turon.data.model.factory.TurnHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.TurnHistoryFragmentBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import com.example.turon.security.viewmodels.TurnHistoryViewModel
import com.example.turon.utils.textChanges
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.turn_history_fragment.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception

class TurnHistoryFragment : Fragment() {
    private var _binding: TurnHistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val orderAdapter by lazy { TurnPagingAdapter() }
    private lateinit var orderList: ArrayList<TurnHistory>
    private var status=true
    private val viewModel: TurnHistoryViewModel by viewModels {
        TurnHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java, requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TurnHistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderList = ArrayList()
        setupUI()
        initAction()
    }


    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        setRecyclerTo()
        setRecyclerVi()
        searchOrderHistory()

    }

    private fun searchOrderHistory() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val editTextFlow = binding.searchView.textChanges()
            editTextFlow
                .debounce(1000)
                .onEach {
                    if (status) getTurnPaginationTo(it.toString())
                    else getHistoryTurn(it.toString())
                }.launchIn(this)

        }

    }

    private fun setRecyclerTo() {
        binding.recyclerTo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderAdapter.retry() }
            )
        }

        orderAdapter.addLoadStateListener { loadStates ->

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

        orderAdapter.setOnClickListener(object : TurnPagingAdapter.OnParcelClickListener {
            override fun clickListener(parcel: TurnHistory) {

            }

        })
    }

    private fun setRecyclerVi() {
        binding.recyclerVi.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderAdapter.retry() }
            )
        }

        orderAdapter.addLoadStateListener { loadStates ->

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

        orderAdapter.setOnClickListener(object : TurnPagingAdapter.OnParcelClickListener {
            override fun clickListener(parcel: TurnHistory) {

            }

        })
    }


    private fun getHistoryTurn(text: String) {
        binding.recyclerVi.isVisible=true
        binding.recyclerTo.isVisible=false
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getTurnPagination(text)
            response.collect {
                orderAdapter.submitData(it)
            }
        }
    }


    private fun getTurnPaginationTo(text: String) {
        binding.recyclerVi.isVisible=false
        binding.recyclerTo.isVisible=true
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getTurnPaginationTo(text)
            response.collect {
                orderAdapter.submitData(it)

            }
        }
    }


    private fun initAction() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShahar.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#FFCC66"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#E4D2B6"))
            status=true
            getTurnPaginationTo("")
            // status = 1
        }
        binding.btnViloyat.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#E4D2B6"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#FFCC66"))
            status=false
            getHistoryTurn("")
//            status = 0
        }


    }

}