package com.example.turon.scales.ui.product_acceptance

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.AcceptanceAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.factory.ProductAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.databinding.FragmentProductAcceptanceBinding
import com.example.turon.auth.AuthActivity
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class ProductAcceptanceFragment : Fragment(), AcceptanceAdapter.OnProductAcceptListener {
    private var _binding: FragmentProductAcceptanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var list: ArrayList<ProductAcceptData>
    private lateinit var progressDialog: AlertDialog
    private val viewModel: ProductAcceptViewModel by viewModels {
        ProductAcceptViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    private lateinit var adapter: AcceptanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductAcceptanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerAcceptance.setHasFixedSize(true)
        list = ArrayList()
        setupUI()
        progressDialog.show()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()


        binding.logout.setOnClickListener {
            val popupMenu=PopupMenu(requireContext(),it)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.out->{
                        SharedPref(requireContext()).setFirstEnter(true)
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                        requireActivity().finishAffinity()
                        true
                    }
                    else->false
                }
            }
            popupMenu.inflate(R.menu.option_menu)
            try {
                val fieldMPopup=PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible=true
                val mPopup=fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                    .invoke(mPopup,true)
            }catch (e:Exception){
                Log.d("TAG","Error show menu icon")
            }finally {
                popupMenu.show()
            }
        }
    }

    private fun getActiveAkt() {
        lifecycleScope.launchWhenStarted {
            viewModel.getActiveAkt()
            viewModel.activeAktState.collect {
                when (it) {
                    is UIState.Success -> {
                        list.clear()
                        list.addAll(it.data)
                        adapter = AcceptanceAdapter(
                            this@ProductAcceptanceFragment,
                            requireContext(),
                            list
                        )
                        binding.recyclerAcceptance.adapter = adapter

                        progressDialog.dismiss()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
            }

        }
    }

    override fun onItemProductAcceptListener(position: Int, productAcceptData: ProductAcceptData) {
        val bundle = bundleOf(
            "id" to productAcceptData.id.toString(),
            "aktName" to productAcceptData.name,
            "clientName" to productAcceptData.client.compony,
            "wagonCount" to productAcceptData.vagonSoni.toString(),
            "stansiya" to productAcceptData.stansiya,
            "title" to "Tovar qabuli"
        )
        findNavController().navigate(
            R.id.action_productAcceptanceFragment_to_scalesHistoryDetailsFragment,
            bundle
        )
    }

    override fun onResume() {
        super.onResume()
        getActiveAkt()
    }

}