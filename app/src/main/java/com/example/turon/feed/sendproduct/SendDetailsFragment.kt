package com.example.turon.feed.sendproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.OrderBaskedAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.OrderBasked
import com.example.turon.data.model.RequestPro
import com.example.turon.data.model.SharedViewModel
import com.example.turon.data.model.factory.FeedSendViewModelFactory
import com.example.turon.data.model.factory.ProductionViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.FragmentSendDetailsBinding
import com.example.turon.databinding.ItemAcceptDialogBinding
import com.example.turon.production.viewmodels.ProductionViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class SendDetailsFragment : Fragment(), OrderBaskedAdapter.OnOrderBaskedClickListener {
    private var _binding: FragmentSendDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderList: ArrayList<OrderBasked>
    private lateinit var adapter: OrderBaskedAdapter
    private var orderId: Int? = null
    private var clientNames: String? = null
    private var userId: Int? = null
    private val cargoList by lazy { ArrayList<TegirmonData>() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SendProductViewModel by viewModels {
        FeedSendViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    private val viewModelSend: ProductionViewModel by viewModels {
        ProductionViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = requireArguments().getInt("orderId")
        clientNames = requireArguments().getString("clientName")
        userId = SharedPref(requireContext()).getUserId()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        binding.appBarTitle.text = clientNames
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
        val userId = SharedPref(requireContext()).getUserId()
        lifecycleScope.launchWhenStarted {
            viewModel.getOrderDetails(userId, orderId!!)
            viewModel.orderDetailsState.collect {
                when (it) {
                    is UIState.Success -> {
                        orderList.clear()
                        orderList.addAll(it.data)
                        setRecycler()
                        progressDialog.dismiss()
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

    private fun postLoadOrder(orderId: Int, baskId: Int) {
        lifecycleScope.launchWhenStarted {
            viewModelSend.postLoadOrder(RequestPro(baskId, orderId))
            viewModelSend.loadOrderState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        val bundle = bundleOf(
                            "orderId" to orderId
                        )
                        findNavController().navigate(
                            R.id.action_sendDetailsFragment_to_sendOrderFinalFragment, bundle
                        )
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

    private fun setRecycler() {
        with(binding) {
            if (orderList.isEmpty()) {
                recyclerSendDetails.isVisible = false
                infoTxt.isVisible = true
            } else {
                recyclerSendDetails.isVisible = true
                infoTxt.isVisible = false
            }

        }

        adapter = OrderBaskedAdapter(orderList, this@SendDetailsFragment)
        binding.recyclerSendDetails.adapter = adapter
    }

    private fun initAction() {
        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sendHistory -> {
                        findNavController().navigate(R.id.feedSendHistoryFragment)
                        true
                    }
                    R.id.brandBalance -> {
                        findNavController().navigate(R.id.brandBalanceFragment)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_balance)
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

    @SuppressLint("SetTextI18n")
    private fun showDialogAccept(productName: String, count: String?, baskId: Int, orderId: Int) {
        val bind: ItemAcceptDialogBinding = ItemAcceptDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = productName
        bind.text.text = "$count Qop"
        bind.textView35.setOnClickListener {
            builder.dismiss()
            postLoadOrder(orderId, baskId)
        }
        bind.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    override fun onItemClickOrderBasked(data: OrderBasked) {
        showDialogAccept(data.product, data.bagsCount.toString(), data.id, orderId!!)

//        val bundle = bundleOf(
//            "orderId" to orderId
//        )
//        findNavController().navigate(
//            R.id.action_sendDetailsFragment_to_sendOrderFinalFragment,
//            bundle
//        )
    }

//    private fun showDialogSendSuccess(count: Int) {
//        val cargoAdapter= SpinnerCargoManAdapter(requireContext(), cargoList)
//        val bind: ItemSendSuccessDialogBinding =
//            ItemSendSuccessDialogBinding.inflate(layoutInflater)
//        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
//        dialog.setCancelable(false)
//        dialog.setView(bind.root)
//        val builder = dialog.create()
//        bind.dialogTitle.text = productName
//        bind.text.text = "$count"
//        bind.desc.text = "Buyurtma yakunlandi"
//        bind.spinnerCargo.adapter=cargoAdapter
//        bind.spinnerCargo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                cargoId = cargoList[position].id
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//        }
//        bind.textView35.setOnClickListener {
//            builder.dismiss()
//            //postLoadOrder()
//
//        }
//        bind.btnClose.setOnClickListener {
//            builder.dismiss()
//        }
//        builder.show()
//    }

}