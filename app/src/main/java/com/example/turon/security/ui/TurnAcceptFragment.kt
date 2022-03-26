package com.example.turon.security.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.TurnAdapter
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.data.model.Turn
import com.example.turon.data.model.factory.TurnAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.TurnAcceptFragmentBinding
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import com.example.turon.utils.SharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class TurnAcceptFragment : Fragment(), TurnAdapter.OnOrderClickListener {
    private var _binding: TurnAcceptFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderAdapter: TurnAdapter
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private lateinit var orderList: ArrayList<Turn>
    private val viewModel: TurnAcceptViewModel by viewModels {
        TurnAcceptViewModelFactory(
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
        _binding = TurnAcceptFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderList = ArrayList()
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerTurn.setHasFixedSize(true)
        addProductObserves()
        initAction()
    }

    private fun initAction() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var queryList: ArrayList<Turn> = ArrayList()
                if (charSequence == "") {
                    orderAdapter = TurnAdapter(orderList, this@TurnAcceptFragment)
                    binding.recyclerTurn.adapter = orderAdapter
                } else {
                    queryList = ArrayList()
                    for (model in orderList) {
                        if (model.toString().lowercase()
                                .contains(charSequence.toString().lowercase())
                        ) {
                            queryList.add(model)
                        }
                    }
                    orderAdapter = TurnAdapter(queryList, this@TurnAcceptFragment)
                    binding.recyclerTurn.adapter = orderAdapter
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })


        binding.logout.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.logout -> {
                        SharedPref(requireContext()).setFirstEnter(true)
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finishAffinity()
                        true
                    }
                    R.id.onlyTurns -> {
                        findNavController().navigate(R.id.activeLoadingFragment)
                        true
                    }
                    R.id.inTurns -> {
                        findNavController().navigate(R.id.activeTurnFragment)
                        true
                    }
                    R.id.historyTurns -> {
                        findNavController().navigate(R.id.turnHistoryFragment)
                        true
                    }
                    R.id.returned -> {
                        findNavController().navigate(R.id.returnedSecurityFragment)
                        true
                    }
                    R.id.succesHistory -> {
                        findNavController().navigate(R.id.succesHistoryFragment)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_turn)
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

    private fun addProductObserves() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getTurnAccept(sharedPref.getUserId())
            viewModel.turnState.collect {
                when (it) {
                    is UIState.Success -> {
                        orderList.clear()
                        progressDialog.dismiss()
                        orderList.addAll(it.data)
                        orderAdapter = TurnAdapter(orderList, this@TurnAcceptFragment)
                        binding.recyclerTurn.adapter = orderAdapter
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

    override fun onItemClickOrder(data: Turn) {
        val bundle = bundleOf(
            "OrderId" to data.id,
            "ClientName" to data.mijoz,
        )
        findNavController().navigate(
            R.id.action_turnAcceptFragment_to_turnAcceptDetailsFragment,
            bundle
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onReject(data: Turn) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.reject))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                model.reject(data.id).observe(viewLifecycleOwner) {
                    if (it.success == true) {
                        Toast.makeText(requireContext(), "O'chirildi", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        addProductObserves()
                    } else {
                        Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
            .show()
    }
}