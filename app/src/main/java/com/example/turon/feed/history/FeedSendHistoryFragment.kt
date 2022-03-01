package com.example.turon.feed.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.turon.adapter.FeedQopHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.OrderHistory
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentFeedSendHistoryBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FeedSendHistoryFragment : Fragment() {
    private var _binding: FragmentFeedSendHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    lateinit var orderHistoryAdapter: FeedQopHistoryAdapter
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
    ): View {
        _binding = FragmentFeedSendHistoryBinding.inflate(inflater, container, false)
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
        binding.recyclerOrder.setHasFixedSize(true)
        getAcceptHistory()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAcceptHistory() {
        progressDialog.show()
        val userId = SharedPref(requireContext()).getUserId()
        Log.e("user_id", "getAcceptHistory: ${userId}", )
        GlobalScope.launch(Dispatchers.Main) {
            val feedQopChiqimHistory = ApiClient.apiService.getFeedQopChiqimHistory(userId)
            if (feedQopChiqimHistory.isSuccessful) {
                orderHistoryAdapter =
                    FeedQopHistoryAdapter(feedQopChiqimHistory.body()!!.qop_chiqim)
                binding.recyclerOrder.adapter = orderHistoryAdapter
                orderHistoryAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }
        }
    }
}