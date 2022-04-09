package com.example.turon.un_receive

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.turon.R
import com.example.turon.adapter.ReceiveAdapter
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
import com.example.turon.databinding.FragmentUnReceiveBinding
import com.example.turon.databinding.TurnAcceptFragmentBinding
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog

class UnReceiveFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentUnReceiveBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var receiveAdapter: ReceiveAdapter
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private lateinit var orderList: ArrayList<Turn>

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
        // Inflate the layout for this fragment
        _binding = FragmentUnReceiveBinding.inflate(inflater, container, false)
        binding.swipeRefresh.setOnRefreshListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        receiveAdapter = ReceiveAdapter()
        binding.recyclerTurn.adapter = receiveAdapter

        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerTurn.setHasFixedSize(true)
        initAction()
    }
    private fun initAction() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var queryList: ArrayList<Turn> = ArrayList()
                if (charSequence == "") {

                } else {
                    queryList = ArrayList()
                    for (model in orderList) {
                        if (model.toString().lowercase()
                                .contains(charSequence.toString().lowercase())
                        ) {
                            queryList.add(model)
                        }
                    }
//                    orderAdapter = TurnAdapter(queryList, this@TurnAcceptFragment)
//                    binding.recyclerTurn.adapter = orderAdapter
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
                    R.id.receive_history -> {
//                        findNavController().navigate(R.id.activeLoadingFragment)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.receive_menu)
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

    override fun onRefresh() {

    }
}