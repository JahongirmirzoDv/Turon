package com.example.turon.production.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.turon.R
import com.example.turon.adapter.*
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.locale.AppDatabase
import com.example.turon.data.model.*
import com.example.turon.data.model.factory.ProductionViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentAcceptanceProBinding
import com.example.turon.production.viewmodels.ProductionViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class AcceptanceProFragment : Fragment(), OnOrderScanListenerF {
    private var _binding: FragmentAcceptanceProBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReturnedProductListAdapter
    private lateinit var listProduct: ArrayList<HistoryProData>
    private lateinit var progressDialog: AlertDialog
    private lateinit var list: ArrayList<CodeScan>
    private lateinit var newList: ArrayList<AcceptProductScanner>

    private val database by lazy { AppDatabase.getRoomClient(requireContext()) }
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private val codeList by lazy { ArrayList<Long>() }
    private val productList by lazy { ArrayList<ProductPro>() }
    private var productType:String=""
    private var baskId: Int? = null

    private val viewModel: ProductionViewModel by viewModels {
        ProductionViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcceptanceProBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()
        newList = ArrayList()
        initList()
        listProduct = ArrayList()
        setupUI()
        initAction()
        getProductPro()

    }

    private fun initAction() {
        binding.etScanTest.showSoftInputOnFocus = false
        binding.etScanTest.requestFocus()
        binding.etScanTest.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                val texts: String =binding.etScanTest.text.toString()
                if (-1 != txt.toString().indexOf("\n")||-1 != txt.toString().indexOf("\r")) {
                    val text = binding.etScanTest.text.toString().replace("\n", "").replace("\r","")


                    doSendPro(text.toLong())

                    binding.etScanTest.setText("")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.ivMenu.setOnClickListener {
            val popupMenu= PopupMenu(requireContext(),it)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.out->{
                        SharedPref(requireContext()).setFirstEnter(true)
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finishAffinity()
                        true
                    }
                    else->false
                }
            }
            popupMenu.inflate(R.menu.option_menu)
            try {
                val fieldMPopup= PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible=true
                val mPopup=fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                    .invoke(mPopup,true)
            }catch (e: Exception){
                Log.d("TAG","Error show menu icon")
            }finally {
                popupMenu.show()
            }
        }

        binding.submitBtn.setOnClickListener {
            setUpObserves()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSendPro(code: Long) {
        if (database.codeDao().exists(code)) {
            Toast.makeText(requireContext(), "Bu maxsulot oldin qabul qilingan", Toast.LENGTH_SHORT)
                .show()
        }
        else {
            if(productType==code.toString().substring(0,2)){
                val codeScan = CodeScan()
                codeScan.code = code
                codeScan.productName = productName.toString()
                codeList.add(code)
                database.codeDao().insertFilter(codeScan)
                list.add(codeScan)
                binding.counter.text=list.size.toString()
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(requireContext(), "Bu mahsulot $productName emas!!!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setUpObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.postItemPro(RequestProC(baskId!!,sharedPref.getUserId(), codeList))
            viewModel.postItemState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.data.data, Toast.LENGTH_SHORT).show()
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

    private fun getProductPro() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getProductPro(sharedPref.getUserId())
            viewModel.productState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        productList.clear()
                        productList.addAll(it.data)
                        setupCustomSpinner(productList)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.counter.text = (codeList.size).toString()
        binding.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)


    var productName: String? = null

    private fun setupCustomSpinner(productList: ArrayList<ProductPro>) {
        val adapterProduct = ProductProAdapter(requireContext(), productList)
        binding.inputScan.adapter = adapterProduct
        binding.inputScan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productName = productList[position].name
                baskId = productList[position].id
                productType=productList[position].prCode.toString()
                list.clear()
                list.addAll(database.codeDao().getAll(productName))
                binding.counter.text=list.size.toString()
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    requireContext(),
                    productType,
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    private fun initList() {
        adapter = ReturnedProductListAdapter(this)
        adapter.submitList(list)
        binding.recyclerAcceptancePro.adapter = adapter

    }

    override fun onItemScanListener(isLong: Boolean) {

    }


}

