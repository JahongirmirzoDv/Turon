package com.example.turon.feed.history

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.ReturnedHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ResultN
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentReturnedHistoryBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ReturnedHistoryFragment : Fragment(){
    private var _binding: FragmentReturnedHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val historyAdapter by lazy { ReturnedHistoryAdapter() }

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
    ): View {
        _binding = FragmentReturnedHistoryBinding.inflate(inflater, container, false)
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
        getReturnedHistory()
        initAction()
    }

    private fun setRecycler() {
        binding.recyclerOrder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { historyAdapter.retry() },
                footer = AdvertLoadStateAdapter { historyAdapter.retry() }
            )
        }



        historyAdapter.addLoadStateListener { loadStates ->

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
        historyAdapter.setOnClickListener(object : ReturnedHistoryAdapter.OnParcelClickListener{
            override fun clickListener(parcel: ResultN) {
                val bundle= bundleOf(
                    "itemId" to parcel.id
                )
                findNavController().navigate(R.id.action_returnedHistoryFragment_to_returnHistoryBaskedFragment,bundle)
            }

        })
    }

    private fun getReturnedHistory() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getReturnedPagination()
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
                    R.id.history -> {
                        findNavController().navigate(R.id.returnedHistoryFragment)

                        true
                    }
                    R.id.returned -> {
                        findNavController().navigate(R.id.returnedGoodsFragment)

                        true
                    }
                    R.id.returnedPro -> {
                        //val bundle= bundleOf("orderId" to orderId)
                        findNavController().navigate(R.id.returnedProductionFragment)

                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_returned)
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