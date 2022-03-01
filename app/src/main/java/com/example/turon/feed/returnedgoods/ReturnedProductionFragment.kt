package com.example.turon.feed.returnedgoods

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.*
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.locale.AppDatabase
import com.example.turon.data.model.*
import com.example.turon.data.model.factory.ProductionViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.*
import com.example.turon.production.viewmodels.ProductionViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class ReturnedProductionFragment : Fragment(), OnOrderScanListenerF {
    private var _binding: FragmentReturnedProductionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReturnedProductListAdapter
    private lateinit var listProduct: ArrayList<HistoryProData>
    private lateinit var progressDialog: AlertDialog
    private lateinit var list: ArrayList<CodeScan>
    private lateinit var newList: ArrayList<AcceptProductScanner>
    private val database by lazy { AppDatabase.getRoomClient(requireContext()) }
    private val codeList by lazy { ArrayList<Long>() }
    private val productList by lazy { ArrayList<ProductPro>() }
    private val cargoList by lazy { ArrayList<TegirmonData>() }
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private var productType: String = ""
    private var bagCount: Int? = null


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
        _binding = FragmentReturnedProductionBinding.inflate(inflater, container, false)
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
        binding.etScanTest.showSoftInputOnFocus = false;
        binding.etScanTest.requestFocus()
        binding.etScanTest.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                val texts: String = binding.etScanTest.text.toString()
                if (-1 != txt.toString().indexOf("\n") || -1 != txt.toString().indexOf("\r")) {
                    val text =
                        binding.etScanTest.text.toString().replace("\n", "").replace("\r", "")

                    doSendPro(text.toLong())

                    binding.etScanTest.setText("")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.history -> {
                        findNavController().navigate(R.id.returnedProHistoryFragment)

                        true
                    }

                    R.id.newRPro -> {
                        findNavController().navigate(R.id.returnedProductionFragment)

                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_return_pro)
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
        binding.imgBack.setOnClickListener { requireActivity().onBackPressed() }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSendPro(code: Long) {
        if (database.codeDao().exists(code)) {
            Toast.makeText(requireContext(), "Bu maxsulot oldin qabul qilingan", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (productType == code.toString().substring(0, 2)) {
                setRecycler(code)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bu mahsulot $productName emas!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    private fun setRecycler(code: Long) {
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

    private fun getProductPro() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getProductPro(SharedPref(requireContext()).getUserId())
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
        progressDialog = SpotsDialog
            .Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()


        binding.counter.text = (codeList.size).toString()

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.submitBtn.setOnClickListener {
            showDialogAccept()
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
                bagCount = 0
                productName = productList[position].name
                productType = productList[position].prCode.toString()
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

    @SuppressLint("SetTextI18n")
    private fun showDialogAccept() {
        val bind: ItemReturnProductDialogBinding =
            ItemReturnProductDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = productName
        bind.text.text = "${codeList.size} Qop"
        bind.textView35.setOnClickListener {
            builder.dismiss()
            postReturnProduct(bind.comment.text.toString())
        }
        bind.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun postReturnProduct(comment: String) {
        val code = RequestReturnProduct(
            sharedPref.getUserId(),
            comment,
            codeList
        )
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.postReturnProduct(code)
            viewModel.postReturnItemState.collect { item ->
                when (item) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), item.data.data, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), item.error, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun initList() {
        adapter = ReturnedProductListAdapter(this)
        adapter.submitList(list)
        binding.recyclerAcceptancePro.adapter = adapter

        binding.allCheck.setOnCheckedChangeListener { _, _ ->
//            adapter.selectAll(isChecked)

        }
        binding.scanRemove.setOnClickListener {
//            list.filter { it.isSelected }
//            adapter.submitList(list.filter { !it.isSelected })
//            adapter.selectionMode(false)
//            binding.scanRemove.isVisible = false
//            binding.checkBoxLayout.isVisible = false
//            binding.scanIcon.isVisible = true
//            binding.counter.isVisible = true
        }


    }

    override fun onItemScanListener(isLong: Boolean) {
    }


}