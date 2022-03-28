package com.example.turon.feed_security

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.CommodityAcceptAdapter
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Acceptance
import com.example.turon.data.model.factory.FeedAcceptanceViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentCommodityAccepttanceFeedSecurityBinding
import com.example.turon.databinding.ItemAcceptDialogBinding
import com.example.turon.feed.commodityacceptance.AcceptanceViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect

class CommodityAccepttanceFeedSecurityFragment : Fragment(),
    CommodityAcceptAdapter.OnAcceptanceClickListener {
    private var _binding: FragmentCommodityAccepttanceFeedSecurityBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CommodityAcceptAdapter
    private lateinit var list: ArrayList<Acceptance>
    private lateinit var progressDialog: AlertDialog
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private val viewModel: AcceptanceViewModel by viewModels {
        FeedAcceptanceViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentCommodityAccepttanceFeedSecurityBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()
        if (sharedPref.getUserType() == "Main_Feed") {
        }
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerAcceptNew.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAcceptNew.setHasFixedSize(true)
        getNewAccept()
        initAction()
    }

    private fun getNewAccept() {
        val userId = SharedPref(requireContext()).getUserId()
        lifecycleScope.launchWhenStarted {
            progressDialog.show()
            viewModel.getNewAccept(userId)
            viewModel.newAcceptState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.cancel()
                        list.clear()
                        list.addAll(it.data)
                        adapter = CommodityAcceptAdapter(
                            list,
                            this@CommodityAccepttanceFeedSecurityFragment
                        )
                        binding.recyclerAcceptNew.adapter = adapter
                    }
                    is UIState.Error -> {
                        progressDialog.cancel()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogAccept(productName: String, count: String?, storeId: Int) {
        val bind: ItemAcceptDialogBinding = ItemAcceptDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = productName
        bind.text.text = "$count Qop"
        bind.textView35.setOnClickListener {
            builder.dismiss()
            postAcceptProduct(storeId)
        }
        bind.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun postAcceptProduct(storeId: Int) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.postAcceptProduct(storeId)
            viewModel.addStoreState.collect {
                when (it) {
                    is UIState.Success -> {
                        Toast.makeText(requireContext(), it.data.data, Toast.LENGTH_SHORT).show()
                        getNewAccept()
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

    @SuppressLint("DiscouragedPrivateApi")
    private fun initAction() {
        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.newAccept -> {
                        findNavController().navigate(R.id.commodityAccepttanceFeedSecurityFragment)
                        true
                    }
                    R.id.history -> {
                        findNavController().navigate(R.id.feedAcceptHistoryFragment)
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
                    R.id.security_turnAccept -> {
                        findNavController().navigate(R.id.turnAcceptFragment2)
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
            popupMenu.inflate(R.menu.feed_security_accept)
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

    override fun onItemClickAcceptance(position: Acceptance) {
        showDialogAccept(position.product, position.bagsCount.toString(), position.id)
    }
}