package com.example.turon.security.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.ReturnedSecurityAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ReturnedSec
import com.example.turon.data.model.factory.FeedAcceptanceHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentReturnedSecurityBinding
import com.example.turon.databinding.ItemReturnedSecBinding
import com.example.turon.feed.history.FeedAcceptHistoryViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.item_returned_sec.view.*
import kotlinx.coroutines.flow.collect

class ReturnedSecurityFragment : Fragment(), ReturnedSecurityAdapter.OnReturnBaskedClickListener {
    private var _binding: FragmentReturnedSecurityBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val historyList by lazy { ArrayList<ReturnedSec>() }
    private var returnedId: Int? = null
    private val adapter by lazy {
        ReturnedSecurityAdapter(
            historyList,
            this@ReturnedSecurityFragment
        )
    }
    private val viewModel: FeedAcceptHistoryViewModel by viewModels {
        FeedAcceptanceHistoryViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReturnedSecurityBinding.inflate(inflater, container, false)
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
//            val popupMenu = PopupMenu(requireContext(), it)
//            popupMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.history -> {
//                        findNavController().navigate(R.id.returnedHistoryFragment)
//
//                        true
//                    }
//                    R.id.returned -> {
//                        findNavController().navigate(R.id.returnedGoodsFragment)
//
//                        true
//                    }
//                    R.id.returnedPro -> {
//                        //val bundle= bundleOf("orderId" to orderId)
//                        findNavController().navigate(R.id.returnedProductionFragment)
//
//                        true
//                    }
//                    else -> false
//                }
//            }
//            popupMenu.inflate(R.menu.option_menu_returned)
//            try {
//                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
//                fieldMPopup.isAccessible = true
//                val mPopup = fieldMPopup.get(popupMenu)
//                mPopup.javaClass
//                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                    .invoke(mPopup, true)
//            } catch (e: Exception) {
//                Log.d("TAG", "Error show menu icon")
//            } finally {
//                popupMenu.show()
//            }
        }
    }

    private fun showDialogFixed(product: String, count: String, comment: String,id:Int) {
        val bind: ItemReturnedSecBinding = ItemReturnedSecBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Mahsulot : $product"
        bind.text.text = "Qop soni : $count"
        bind.textComment.text = "Izoh : $comment"
        bind.textView35.setOnClickListener {
            builder.dismiss()
            addReturned(id)
        }
        builder.show()
    }

    private fun addReturned(product_id: Int) {
        val map: HashMap<String, Any> = HashMap()
        map["id"]=product_id
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.postconfirmReturned(map)
            viewModel.confirmReturnedState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Bajarildi", Toast.LENGTH_SHORT).show()
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

    private fun getHistory() {
        val userId = SharedPref(requireContext()).getUserId()
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getReturnedSec()
            viewModel.returnSec.collect {
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

    override fun onItemClickReturnBasked(position: ReturnedSec) {
        showDialogFixed(position.product, position.qopsoni.toString(), position.izoh,position.id)

    }


}