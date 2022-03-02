package com.example.turon.security.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.turon.R
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.adapter.TurnDetailsAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.SharedViewModel
import com.example.turon.data.model.factory.TurnAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.*
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import com.example.turon.utils.SharedPref
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect

class TurnAcceptDetailsFragment : Fragment(), TurnDetailsAdapter.OnOrderClickListener {
    private var _binding: FragmentTurnAcceptDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderList: ArrayList<ClientData>
    private lateinit var cityList: ArrayList<TegirmonData>
    private lateinit var adapter: TurnDetailsAdapter
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private var orderId: Int? = null
    private var clientName: String? = null
    private var phone: String? = null

    private var userId: Int? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: TurnAcceptViewModel by viewModels {
        TurnAcceptViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = requireArguments().getInt("OrderId")
        clientName = requireArguments().getString("ClientName")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTurnAcceptDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        setupUI()
        progressDialog.show()

    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()

        orderList = ArrayList()
        cityList = ArrayList()
        binding.recyclerSendDetails.setHasFixedSize(true)
        binding.btnSend.setOnClickListener {
            showDialog()
        }
        getOrderDetails()
        binding.appBarTitle.text = clientName ?: ""
    }

    private fun getOrderDetails() {
        lifecycleScope.launchWhenStarted {
            viewModel.getTurnClient(orderId!!)
            viewModel.turnDetailState.collect {
                when (it) {
                    is UIState.Success -> {
                        orderList.clear()
                        orderList.addAll(it.data)
//                        sharedViewModel.sendOrderDetail(orderList)
                        adapter = TurnDetailsAdapter(orderList, this@TurnAcceptDetailsFragment)
                        progressDialog.dismiss()
                        binding.recyclerSendDetails.adapter = adapter
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

    private fun addTurn(turnNum: String, carNum: String?, cityId: Int, phone: String) {
        val map = HashMap<String, Any>()
        map["order_id"] = orderId.toString()
        map["status"] = cityId
        map["turn"] = turnNum
        map["car_number"] = carNum!!
        map["driver_phone"] = phone
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.addTurn(map)
            viewModel.addTurnState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        if (cityId==1){
                            sharedPref.setTurnNumToshkent(sharedPref.getTurnNumToshkent() + 1)
                        }else{
                            sharedPref.setTurnNumViloyat(sharedPref.getTurnNumViloyat() + 1)
                        }

                        showDialogFixed(turnNum, clientName)
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


    private fun initAction() {
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnSend.setOnClickListener {
//            postReturnedGoods()
        }


    }

    override fun onItemClickOrderDetails(data: ClientData) {

    }

    private fun showDialog() {
        val bind: TurnDialogBinding = TurnDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = clientName
        val listener = MaskedTextChangedListener("[00] [000]-[00]-[00]", bind.text4)
        bind.text4.addTextChangedListener(listener)
        bind.text4.onFocusChangeListener = listener
        cityList.add(TegirmonData(1,"Toshkent shahri"))
        cityList.add(TegirmonData(0,"Viloyatlar"))
        val cityAdapter=SpinnerCargoManAdapter(requireContext(),cityList)
        bind.text10.adapter=cityAdapter
        var cityId=0

        bind.text10.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                cityId=cityList[position].id
                if (cityId==1){
                    bind.text3.text = sharedPref.getTurnNumToshkent().toString()
                }else {
                    bind.text3.text = sharedPref.getTurnNumViloyat().toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        bind.textView35.setOnClickListener {
            val turnId = bind.text3.text.toString()
            val carNum = bind.text1.text.toString()
            val phone = bind.text4.text.toString()

            when {
                carNum.length < 6 -> {
                    Toast.makeText(
                        requireContext(),
                        "Mashina raqamini to`liq kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }



                phone.length < 12 -> {
                    Toast.makeText(
                        requireContext(),
                        "Telefon raqamini to`liq kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    builder.dismiss()
                    addTurn(turnId, carNum.uppercase(), cityId, phone)
                }
            }

        }
        builder.show()

    }

    @SuppressLint("SetTextI18n")
    private fun showDialogFixed(turnNum: String, clientName: String?) {
        val bind: TurnNumDialogBinding = TurnNumDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Mijoz : $clientName"
        bind.text.text = "№ $turnNum nomer ostida\n navbatga olindi."
        bind.textView35.setOnClickListener {
            builder.dismiss()
            requireActivity().onBackPressed()
        }
        builder.show()
    }
}