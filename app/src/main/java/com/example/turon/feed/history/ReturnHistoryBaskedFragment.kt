package com.example.turon.feed.history

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.App
import com.example.turon.R
import com.example.turon.adapter.ReturnedBaskedAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ReturnedBasked
import com.example.turon.data.model.factory.FeedAcceptanceHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentReturnHistoryBaskedBinding
import com.example.turon.feed.sendproduct.SendProductViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class ReturnHistoryBaskedFragment : Fragment(), ReturnedBaskedAdapter.OnReturnBaskedClickListener {
    private var _binding: FragmentReturnHistoryBaskedBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val historyList by lazy { ArrayList<ReturnedBasked>() }
    private var returnedId: Int? = null
    private val adapter by lazy {
        ReturnedBaskedAdapter(
            historyList,
            this@ReturnHistoryBaskedFragment
        )
    }
    private val viewModel: FeedAcceptHistoryViewModel by viewModels {
        FeedAcceptanceHistoryViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        returnedId=requireArguments().getInt("itemId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReturnHistoryBaskedBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
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
        binding.recyclerHistoryPro.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHistoryPro.setHasFixedSize(true)
        getHistory()
        initAction()
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

    private fun getHistory() {
        val userId= SharedPref(requireContext()).getUserId()
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getReturnedBasked(userId,returnedId!!)
            viewModel.returnBasked.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        historyList.clear()
                        historyList.addAll(it.data)
                        binding.recyclerHistoryPro.adapter = adapter
                        Toast.makeText(requireContext(), "it", Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onItemClickReturnBasked(position: ReturnedBasked) {

    }


}