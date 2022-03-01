package com.example.turon.feed.returnedgoods

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.turon.R
import com.example.turon.adapter.OrderDetailsAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Balance
import com.example.turon.data.model.SharedViewModel
import com.example.turon.data.model.factory.FeedReturnedGoodsViewModelFactory
import com.example.turon.data.model.factory.FeedSendViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.OrderDetailsData
import com.example.turon.databinding.DialogItemBinding
import com.example.turon.databinding.FragmentReturnGoodsDetailsBinding
import com.example.turon.feed.sendproduct.SendProductViewModel
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import okhttp3.MultipartBody

class ReturnGoodsDetailsFragment : Fragment(), OrderDetailsAdapter.OnOrderClickListener {
    private var _binding: FragmentReturnGoodsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderList: ArrayList<OrderDetailsData>
    private lateinit var adapter: OrderDetailsAdapter
    private var orderId: Int? = null
    private var userId: Int? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ReturnedGoodsViewModel by viewModels {
        FeedReturnedGoodsViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = requireArguments().getInt("orderId")
        userId = requireArguments().getInt("userId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReturnGoodsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        setupUI()
        progressDialog.show()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()

        orderList = ArrayList()
        binding.recyclerSendDetails.setHasFixedSize(true)
        getOrderDetails()

    }

    private fun getOrderDetails() {
//        lifecycleScope.launchWhenStarted {
//            viewModel.getReturnedGoodsClient(orderId!!,userId!!)
//            viewModel.returnedGoodsDetailState.collect {
//                when (it) {
//                    is UIState.Success -> {
////                        orderList.addAll(it.data)
////                        sharedViewModel.sendOrderDetail(orderList)
////                        adapter = OrderDetailsAdapter(orderList, this@ReturnGoodsDetailsFragment)
////                        progressDialog.dismiss()
////                        binding.recyclerSendDetails.adapter = adapter
//                    }
//                    is UIState.Error -> {
//                        progressDialog.dismiss()
//                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                    }
//                    is UIState.Loading, UIState.Empty -> Unit
//
//                }
//            }
//
//        }
    }

//    private fun postReturnedGoods() {
//        val builder: MultipartBody.Builder = MultipartBody.Builder()
//        builder.setType(MultipartBody.FORM)
//
//        builder.addFormDataPart("qaytuv_id", orderId!!.toString())
//        orderList.forEach {
//            builder.addFormDataPart("bask_id", it.id.toString())
//        }
//        val body = builder.build()
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.postReturnedGoods(body)
//            viewModel.postReturnedGoodsState.collect {
//                when (it) {
//                    is UIState.Success -> {
//                        progressDialog.dismiss()
//                        requireActivity().onBackPressed()
//                    }
//                    is UIState.Error -> {
//                        progressDialog.dismiss()
//                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                    }
//                    is UIState.Loading, UIState.Empty -> Unit
//
//                }
//            }
//
//        }
//    }

    private fun initAction() {
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnSend.setOnClickListener {
//            postReturnedGoods()
        }


    }

    override fun onItemClickOrderDetails(data: Balance) {

    }

//    override fun onItemClickOrderDetails(data: OrderDetailsData) {
//
//    }

}