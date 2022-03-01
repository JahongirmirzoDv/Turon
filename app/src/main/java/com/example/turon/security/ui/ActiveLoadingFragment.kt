package com.example.turon.security.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.turon.R
import com.example.turon.adapter.ActivTurnAdapter
import com.example.turon.adapter.TurnAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Turn
import com.example.turon.data.model.factory.TurnAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.Activetashkent
import com.example.turon.data.model.response.Activeviloyat
import com.example.turon.databinding.FragmentActiveLoadingBinding
import com.example.turon.databinding.ItemTurnDialog2Binding
import com.example.turon.databinding.TurnAcceptFragmentBinding
import com.example.turon.databinding.TurnNumDialogBinding
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class ActiveLoadingFragment : Fragment(), ActivTurnAdapter.OnHistoryClickListener {
    private var _binding: FragmentActiveLoadingBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: ActivTurnAdapter
    private lateinit var activeListVi: ArrayList<Activetashkent>
    private lateinit var activeListTo: ArrayList<Activetashkent>
    private lateinit var loadingListTo: ArrayList<Activetashkent>
    private lateinit var loadingListVi: ArrayList<Activetashkent>

    private var status: Int = 1
    private lateinit var progressDialog: AlertDialog
    private val viewModel: TurnAcceptViewModel by viewModels {
        TurnAcceptViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActiveLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activeListTo = ArrayList()
        activeListVi = ArrayList()
        loadingListTo = ArrayList()
        loadingListVi = ArrayList()
        initAction()
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerTurn.setHasFixedSize(true)
        getHistory()
    }

    private fun initAction() {
        binding.btnShahar.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#FFCC66"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#E4D2B6"))
            getHistoryClick("Toshkent")
            status = 1
        }
        binding.btnViloyat.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#E4D2B6"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#FFCC66"))
            getHistoryClick("")
            status = 0
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getHistory() {
        lifecycleScope.launchWhenStarted {
            viewModel.getTurnHistory()
            viewModel.turnHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        activeListVi.clear()
                        activeListTo.clear()
                        activeListVi.addAll(it.data.data.activeviloyat)
                        activeListTo.addAll(it.data.data.activetashkent)
                        orderAdapter = ActivTurnAdapter(activeListTo, this@ActiveLoadingFragment)
                        binding.recyclerTurn.adapter = orderAdapter
                    }
                    is UIState.Error -> {

                    }
                    else -> Unit
                }
            }


        }
    }

    private fun getHistoryClick(click: String) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getTurnHistory()
            viewModel.turnHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        activeListVi.clear()
                        activeListTo.clear()
                        activeListVi.addAll(it.data.data.activeviloyat)
                        activeListTo.addAll(it.data.data.activetashkent)
                        progressDialog.dismiss()
                        if (click == "Toshkent") {
                            orderAdapter =
                                ActivTurnAdapter(activeListTo, this@ActiveLoadingFragment)
                            binding.recyclerTurn.adapter = orderAdapter
                        } else {
                            orderAdapter =
                                ActivTurnAdapter(activeListVi, this@ActiveLoadingFragment)
                            binding.recyclerTurn.adapter = orderAdapter
                        }

                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                    }
                    else -> Unit
                }
            }


        }

    }


    override fun onItemClickOrder(position: Activetashkent) {
        showDialogFixed(position)
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogFixed(position: Activetashkent) {
        val bind: ItemTurnDialog2Binding = ItemTurnDialog2Binding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Mijoz : ${position.mijoz}"
        bind.dialogTitle2.text = "№ ${position.turn} navbati keldi"
        bind.textView35.setOnClickListener {
            builder.dismiss()
            turnInsert(position.id,position.status)
        }
        builder.show()
    }

    private fun turnInsert(turnNum: Int,st:Int) {
        val map = HashMap<String, Any>()
        map["turn_id"] = turnNum
        map["status"] = st
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.insertTurns(map)
            viewModel.insertTurn.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        getHistory()
//                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
//
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