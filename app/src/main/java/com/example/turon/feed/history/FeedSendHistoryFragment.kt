package com.example.turon.feed.history

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.SendOrderHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.OrderHistory
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentFeedSendHistoryBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.utils.SharedPref
import com.example.turon.utils.textChanges
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.toolbar_default.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FeedSendHistoryFragment : Fragment() {
    private var _binding: FragmentFeedSendHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val orderHistoryAdapter by lazy { SendOrderHistoryAdapter() }
    private val orderList by lazy { ArrayList<OrderHistory>() }

    private val viewModel: AllHistoryViewModel by viewModels {
        AllHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java, requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedSendHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }

    private fun searchOrderHistory() {
        binding.toolbarSearch.etSearch.requestFocus()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val editTextFlow = binding.toolbarSearch.etSearch.textChanges()
            editTextFlow
                .debounce(1000)
                .onEach {
                    getAcceptHistory(it.toString())
                }.launchIn(this)

        }

    }


    private fun hideShowSearch() {
        with(binding) {
            toolbarDefault.search.setOnClickListener {
                val toolbarD: View = toolbarDefault.root
                val toolbarS: View = toolbarSearch.root
                toolbarD.isVisible = false
                toolbarS.isVisible = true
                binding.toolbarSearch.etSearch.requestFocus()
            }
            toolbarSearch.ivBack.setOnClickListener {
                val toolbarD: View = toolbarDefault.root
                val toolbarS: View = toolbarSearch.root
                toolbarD.isVisible = true
                toolbarS.isVisible = false
            }
            toolbarSearch.ivClear.setOnClickListener {
                toolbarSearch.etSearch.setText("")
            }
            toolbarSearch.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    toolbarSearch.ivClear.isVisible = !s.isNullOrEmpty()
//                    toolbarSearch.ivClear.isVisible = s != ""
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerOrder.setHasFixedSize(true)
        binding.appBarLayout.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setRecycler()
        hideShowSearch()
        searchOrderHistory()


    }

    private fun setRecycler() {
        binding.recyclerOrder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderHistoryAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderHistoryAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderHistoryAdapter.retry() }
            )
        }
        orderHistoryAdapter.addLoadStateListener { loadStates ->

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

        orderHistoryAdapter.setOnClickListener(object :
            SendOrderHistoryAdapter.OnParcelClickListener {
            override fun clickListener(parcel: OrderHistory) {

            }

        })

    }

    private fun getAcceptHistory(text: String) {
        val userId = SharedPref(requireContext()).getUserId()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getOrderPagination(text, userId)
            response.collect {
                try {
                    orderHistoryAdapter.submitData(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}