package com.example.turon.feed.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.OrderDetailsAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.data.model.Balance
import com.example.turon.data.model.factory.FeedAcceptanceHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.EditBinding
import com.example.turon.databinding.FragmentFlourBrandBalanceBinding
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.toolbar_default_order.view.*
import kotlinx.coroutines.flow.collect


class BrandBalanceFragment : Fragment(), OrderDetailsAdapter.OnOrderClickListener {
    private var _binding: FragmentFlourBrandBalanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private var bagCount: String = ""
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private val productList by lazy { ArrayList<Balance>() }
    private lateinit var adapter: OrderDetailsAdapter
    private val viewModel: FeedAcceptHistoryViewModel by viewModels {
        FeedAcceptanceHistoryViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
    private val model: ControlViewModel by viewModels {
        ViewModelFactory(
            ApiHelper2(
                ApiClient2.createService(
                    ApiService2::class.java,
                    requireContext()
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlourBrandBalanceBinding.inflate(inflater, container, false)
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
        getBrandBalance()
        hideShowSearch()
        initAction()
    }

    private fun initAction() {
        binding.appBarLayout.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sendHistory -> {
                        findNavController().navigate(R.id.feedSendHistoryFragment)
                        Toast.makeText(requireContext(), "Yuborish tarixi", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.brandBalance -> {
                        findNavController().navigate(R.id.brandBalanceFragment)
                        Toast.makeText(requireContext(), "Tovar qoldiq", Toast.LENGTH_SHORT).show()
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

    private fun hideShowSearch() {
        with(binding) {
            toolbarDefault.appBarTitle.text = "Tovar qoldiq"
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
                    var queryList: ArrayList<Balance> = ArrayList()
                    if (s == "") {
                        adapter = OrderDetailsAdapter(productList, this@BrandBalanceFragment)
                        binding.recyclerSendDetails.adapter = adapter
                    } else {
                        queryList = ArrayList()
                        for (model in productList) {
                            if (model.balanceString().lowercase()
                                    .contains(s.toString().lowercase())
                            ) {
                                queryList.add(model)
                            }
                        }
                        adapter = OrderDetailsAdapter(queryList, this@BrandBalanceFragment)
                        binding.recyclerSendDetails.adapter = adapter
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun getBrandBalance() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            val userId = sharedPref.getUserId()
            viewModel.getBrandBalanceFeed(userId)
            viewModel.brandBalance.collect {
                when (it) {
                    is UIState.Success -> {
                        productList.clear()
                        productList.addAll(it.data)
                        adapter = OrderDetailsAdapter(productList, this@BrandBalanceFragment)
                        binding.recyclerSendDetails.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.recyclerSendDetails.setHasFixedSize(true)
                        binding.recyclerSendDetails.adapter = adapter
                        progressDialog.dismiss()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
            }
        }
    }

    override fun onItemClickOrderDetails(data: Balance) {
        if (sharedPref.getUserType() == "Main_Feed" || sharedPref.getUserType() == "FeedSecurity") {
            showDialog(data)
        } else {
            Toast.makeText(requireContext(), "xato", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(data: Balance) {
        val bind: EditBinding = EditBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "O'zgartirish"
        bind.text0.text = data.productName
        bind.text3.setText("${data.bagCount}")
        bind.textView35.setOnClickListener {
            bagCount = bind.text3.text.toString()
            when {
                bagCount.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Qop Sonini kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    builder.dismiss()
                    addExpense(data.id, bagCount.toFloat())
                }
            }
        }
        builder.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addExpense(bagTypeId: Int, bagCount: Float) {
        progressDialog.show()
        val map: HashMap<String, Any> = HashMap()
        map["user_id"] = sharedPref.getUserId()
        map["un_id"] = bagTypeId
        map["soni"] = bagCount
        lifecycleScope.launchWhenStarted {
            model.edit(map).observe(viewLifecycleOwner) {
                if (it.success == true) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "O'zgartirildi", Toast.LENGTH_SHORT).show()
                    getBrandBalance()
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
        }
    }
}