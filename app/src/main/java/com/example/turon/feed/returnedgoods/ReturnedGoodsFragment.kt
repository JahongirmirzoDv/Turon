package com.example.turon.feed.returnedgoods

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.*
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ProductPro
import com.example.turon.data.model.ReturnBasket
import com.example.turon.data.model.SharedViewModel
import com.example.turon.data.model.factory.FeedReturnedGoodsViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.FragmentReturnedGoodsBinding
import com.example.turon.databinding.ItemAcceptDialogBinding
import com.example.turon.databinding.OrderBasketDialogBinding
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.lang.Exception


class ReturnedGoodsFragment : Fragment(), ReturnProductAdapter.OnOrderClickListener {
    private var _binding: FragmentReturnedGoodsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderAdapter: ReturnProductAdapter
    private lateinit var baskedAdapter: ReturnBasketAdapter
    private lateinit var orderList: ArrayList<OrderData>
    private val basketList by lazy { ArrayList<ReturnBasket>() }
    private val cargoManList by lazy { ArrayList<TegirmonData>() }
    private var storeId: Int? = null
    private var cargoId: Int? = null
    private val viewModel: ReturnedGoodsViewModel by viewModels {
        FeedReturnedGoodsViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReturnedGoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        orderList = ArrayList()
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerReturn.setHasFixedSize(true)
        returnProductObserves()


    }

    private fun returnProductObserves() {
        val userId = SharedPref(requireContext()).getUserId()
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getReturnedGoods(userId)
            viewModel.returnedGoodsState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        orderList.clear()
                        orderList.addAll(it.data)
                        orderAdapter = ReturnProductAdapter(orderList, this@ReturnedGoodsFragment)
                        binding.recyclerReturn.adapter = orderAdapter
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
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

    override fun onItemClickOrder(position: OrderData) {

        getBasket(position.id)
        storeId = position.id

    }

    private fun getBasket(id: Int) {
        progressDialog.show()
        val userId = SharedPref(requireContext()).getUserId()
        lifecycleScope.launchWhenStarted {
            viewModel.getReturnedBasket(userId, id)
            viewModel.returnedBasketState.collect {
                when (it) {
                    is UIState.Success -> {
                        basketList.clear()
                        basketList.addAll(it.data)
                        getCargo()

                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun getCargo() {
        lifecycleScope.launchWhenStarted {
            viewModel.getCargoMan()
            viewModel.cargoManState.collect {
                when (it) {
                    is UIState.Success -> {
                        cargoManList.clear()
                        cargoManList.addAll(it.data)
                        progressDialog.dismiss()
                        showDialogAccept()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun showDialogAccept() {
        val bind: OrderBasketDialogBinding = OrderBasketDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        val adapterProduct = SpinnerCargoManAdapter(requireContext(), cargoManList)
        baskedAdapter = ReturnBasketAdapter(basketList)
        bind.spinner.adapter = adapterProduct
        bind.returnedBasketRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = baskedAdapter
        }
        bind.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cargoId = cargoManList[position].id

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        bind.textView35.setOnClickListener {
            builder.dismiss()
            loadReturnBasket()
        }

        builder.show()
    }

    private fun loadReturnBasket() {
        val builder: MultipartBody.Builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("qaytuv_id", storeId!!.toString())
        builder.addFormDataPart("brigada", cargoId!!.toString())
        basketList.forEach {
            builder.addFormDataPart("bask_id", it.id.toString())
        }
        val body = builder.build()
        lifecycleScope.launchWhenStarted {
            viewModel.postReturnedGoods(body)
            viewModel.postReturnedGoodsState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        returnProductObserves()

                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
            }

        }

    }
}