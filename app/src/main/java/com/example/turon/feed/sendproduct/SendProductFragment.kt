package com.example.turon.feed.sendproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.turon.R
import com.example.turon.adapter.OrderAdapter
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.SharedViewModel
import com.example.turon.data.model.factory.FeedSendViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.OrderData
import com.example.turon.databinding.SendProductFragmentBinding
import com.example.turon.utils.SharedPref
import com.example.turon.utils.SharedPref2
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class SendProductFragment : Fragment(), OrderAdapter.OnOrderClickListener {
    private var _binding: SendProductFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderAdapter: OrderAdapter
    lateinit var layoutManager: LinearLayoutManager
    var lastposition: Int? = null
    var a = false
    private lateinit var orderList: ArrayList<OrderData>
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val sharedPref by lazy { SharedPref(requireContext()) }

    private val viewModel: SendProductViewModel by viewModels {
        FeedSendViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SendProductFragmentBinding.inflate(inflater, container, false)
        layoutManager = LinearLayoutManager(requireContext())
        SharedPref2.getInstanceDis(requireContext())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        orderList = ArrayList()
        setupUI()
        //this is adapter ui type change
        if (sharedPref.getUserType() == "Main_Feed" || sharedPref.getUserType() == "FeedSecurity") {
            binding.status.visibility = View.VISIBLE
            a = true
        }
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerOrder.setHasFixedSize(true)
        addProductObserves()
        hideShowSearch()
    }

    private fun hideShowSearch() {
        with(binding) {
            toolbarDefault.appBarTitle.text = "Buyurtmalar"
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
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    toolbarSearch.ivClear.isVisible = !s.isNullOrEmpty()
                    var queryList: ArrayList<OrderData> = ArrayList()
                    if (s == "") {
                        orderAdapter = OrderAdapter(
                            orderList,
                            sharedPref.getUserType(),
                            this@SendProductFragment
                        )
                        orderAdapter.setT(a)
                        binding.recyclerOrder.layoutManager = layoutManager
                        binding.recyclerOrder.adapter = orderAdapter
                    } else {
                        queryList = ArrayList()
                        for (model in orderList) {
                            if (model.orderString().lowercase()
                                    .contains(s.toString().lowercase())
                            ) {
                                queryList.add(model)
                            }
                        }
                        orderAdapter = OrderAdapter(
                            queryList,
                            sharedPref.getUserType(),
                            this@SendProductFragment
                        )
                        orderAdapter.setT(a)
                        binding.recyclerOrder.layoutManager = layoutManager
                        binding.recyclerOrder.adapter = orderAdapter
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun addProductObserves() {
        val userID = SharedPref(requireContext()).getUserId()
        lifecycleScope.launchWhenStarted {
            progressDialog.show()
            viewModel.getOrder(userID)
            viewModel.orderState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        orderList.clear()
                        orderList.addAll(it.data)
                        orderAdapter = OrderAdapter(
                            orderList,
                            sharedPref.getUserType(),
                            this@SendProductFragment
                        )
                        //this is adapter ui type change
                        orderAdapter.setT(a)

                        binding.recyclerOrder.layoutManager = layoutManager
                        val user = SharedPref2.user
                        binding.recyclerOrder.scrollToPosition(user!!)
                        binding.recyclerOrder.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(
                                recyclerView: RecyclerView,
                                newState: Int,
                            ) {
                                super.onScrollStateChanged(recyclerView, newState)
                                lastposition = layoutManager.findFirstVisibleItemPosition()
                            }
                        })
                        binding.recyclerOrder.adapter = orderAdapter
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

    @SuppressLint("DiscouragedPrivateApi")
    private fun initAction() {
        binding.toolbarDefault.menu.setOnClickListener {
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
                    R.id.logout -> {
                        sharedPref.setFirstEnter(true)
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finishAffinity()
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

    override fun onItemClickOrder(position: OrderData) {
        sharedPref.setClientData(position)
        val bundle = bundleOf(
            "orderId" to position.id,
            "clientName" to position.client
        )
        //Toast.makeText(requireContext(), sharedPref.getClientData().carNum, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_sendProductFragment_to_sendDetailsFragment, bundle)
    }

    override fun onPause() {
        super.onPause()
        try {
            SharedPref2.user = lastposition ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            SharedPref2.user = lastposition ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}