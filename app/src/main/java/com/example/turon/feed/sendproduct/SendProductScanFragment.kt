package com.example.turon.feed.sendproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.*
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.locale.AppDatabase
import com.example.turon.data.locale.CodeDatabase
import com.example.turon.data.model.*
import com.example.turon.data.model.factory.ProductionViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.*
import com.example.turon.production.viewmodels.ProductionViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect


class SendProductScanFragment : Fragment(), OnOrderScanListenerT {
    private var _binding: FragmentSendProductScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderListAdapter
    private lateinit var listProduct: ArrayList<HistoryProData>
    private lateinit var progressDialog: AlertDialog
    private lateinit var list: ArrayList<BrCode>
    private lateinit var newList: ArrayList<AcceptProductScanner>
    private val database by lazy { CodeDatabase.getRoomClient(requireContext()) }
    private val codeList by lazy { ArrayList<Long>() }
    private val productListSp by lazy { ArrayList<LoadOrder>() }
    private val cargoList by lazy { ArrayList<TegirmonData>() }
    private var productType: String = ""
    private var isLast: Boolean = false
    private var status: Boolean = false

    private var bagCount: Int = 0
    private var orderId: Int? = null
    private var baskId: Int? = null
    private var cargoId: Int? = null

    private val viewModel: ProductionViewModel by viewModels {
        ProductionViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = requireArguments().getInt("orderId")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendProductScanBinding.inflate(inflater, container, false)
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
        getCargoMan()

    }

    private fun initAction() {
        //binding.etScanTest.showSoftInputOnFocus = false;
        binding.etScanTest.requestFocus()
        binding.etScanTest.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                val texts: String = binding.etScanTest.text.toString()
                if (-1 != txt.toString().indexOf("\n") || -1 != txt.toString().indexOf("\r")) {
                    val text = binding.etScanTest.text.toString().replace("\n", "").replace("\r", "")

                    //Toast.makeText(requireContext(), text.toString(), Toast.LENGTH_SHORT).show()
                    doSendPro(text.toLong())

                    binding.etScanTest.setText("")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.submitBtn.setOnClickListener {
            //postLoadOrder()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSendPro(code: Long) {
        if (database.codeDao().exists(code)) {
            Toast.makeText(requireContext(), "Bu maxsulot oldin qabul qilingan", Toast.LENGTH_SHORT)
                .show()
        }
        else {
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


    private fun insertData(code: Long) {
        val codeScan = BrCode()
        codeScan.code = code
        codeScan.productName = productName!!
        codeScan.orderId = orderId!!
        codeList.add(code)
        database.codeDao().insertCodes(codeScan)
        list.add(codeScan)
        adapter.submitList(list)
        binding.counter.text = (bagCount - codeList.size).toString()
        adapter.notifyDataSetChanged()
    }

    private fun setRecycler(code: Long) {
        val size5 = bagCount - 5
        when (list.size + 1) {
            size5 -> {
                insertData(code)
                showDialogSend5(size5)
            }
            bagCount -> {
                insertData(code)
                showDialogSendSuccess(bagCount)
            }
            else -> {
                insertData(code)
            }
        }
    }

//    private fun postLoadOrder() {
//        lifecycleScope.launchWhenStarted {
//            viewModel.postLoadOrder(RequestPro(baskId!!, orderId!!, codeList))
//            viewModel.loadOrderState.collect {
//                when (it) {
//                    is UIState.Success -> {
//                        codeList.clear()
//                        progressDialog.dismiss()
//                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
//                        if (isLast){
//                            status=it.data.status
//                            addCargoToBasket()
//                        }
//                        else{
//                            getLoadOrder()
//                        }
//                    }
//                    is UIState.Error -> {
//                        progressDialog.dismiss()
//                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                    }
//                    is UIState.Loading, UIState.Empty -> Unit
//
//                }
//            }
//        }
//
//    }

    private fun addCargoToBasket() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.addCargoToBasket(baskId!!,brigada =cargoId!!)
            viewModel.addCargoState.collect {
                when (it) {
                    is UIState.Success -> {
                        codeList.clear()
                        progressDialog.dismiss()
                        if (status){
                            database.codeDao().deleteAllCodes()
                            val bundle = bundleOf("orderId" to orderId)
                            findNavController().navigate(R.id.action_sendProductScanFragment_to_sendOrderFinalFragment, bundle)
                        }else requireActivity().onBackPressed()
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

    private fun getLoadOrder() {
        val userId = SharedPref(requireContext()).getUserId()
//        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getLoadOrder(userId, orderId!!)
            viewModel.prBaskedState.collect { item ->
                when (item) {
                    is UIState.Success -> {
                        val lastProduct = productListSp.filter { it.status }
                        progressDialog.dismiss()
                        productListSp.clear()
                        productListSp.addAll(item.data)
                        setupCustomSpinner(productListSp)
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), item.error, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
            }
        }

    }

    private fun getCargoMan() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getCargoMan()
            viewModel.cargoState.collect { item ->
                when (item) {
                    is UIState.Success -> {
//                        progressDialog.dismiss()
                        cargoList.clear()
                        cargoList.addAll(item.data)
                        getLoadOrder()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), item.error, Toast.LENGTH_SHORT).show()
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
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    var productName: String? = null

    private fun setupCustomSpinner(productListSp: ArrayList<LoadOrder>) {
        val adapterProduct = LoadOrderSpinnerAdapter(requireContext(), productListSp)
        binding.inputScan.adapter = adapterProduct
        binding.inputScan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baskId = productListSp[position].id
                productName = productListSp[position].product
                productType = productListSp[position].prCode.toString()
                bagCount=0
                productListSp.forEach { bagCount +=it.bagsCount  }
                list.clear()
                if (database.codeDao().existsOrder(orderId!!)){
                    list.addAll(database.codeDao().getAll(orderId!!).filter { it.productName==productName })
                    adapter.submitList(list)
                    adapter.notifyDataSetChanged()
                }


                Toast.makeText(
                    requireContext(),
                    productType.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                binding.counter.text =(bagCount-list.size).toString()
                ///tanlangan item boyicha rec ni tozalab yangi datani set qilish kerak


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSend5(size5: Int) {
        val bind: ItemcargoinfoBinding = ItemcargoinfoBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = productName
        bind.text.text = size5.toString()
        bind.desc.text = "Buyurtmaga yana ${5}\n qop qo`shing!"
        bind.btnClose.setOnClickListener {

            builder.dismiss()
        }
        builder.show()
    }

    private fun showDialogSendSuccess(count: Int) {
       val cargoAdapter= SpinnerCargoManAdapter(requireContext(), cargoList)
        val bind: ItemSendSuccessDialogBinding =
            ItemSendSuccessDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = productName
        bind.text.text = "$count"
        bind.desc.text = "Buyurtma yakunlandi"
        bind.spinnerCargo.adapter=cargoAdapter
        bind.spinnerCargo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cargoId = cargoList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        bind.textView35.setOnClickListener {
            builder.dismiss()
            //postLoadOrder()
            isLast=true
        }
        bind.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun initList() {
        adapter = OrderListAdapter(this)
        adapter.submitList(list)
        binding.recyclerAcceptancePro.adapter = adapter
    }

    override fun onItemScanListener(isLong: Boolean) {

    }


}