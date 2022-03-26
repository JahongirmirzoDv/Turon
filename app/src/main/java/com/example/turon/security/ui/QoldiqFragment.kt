package com.example.turon.security.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.Qoldiq
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.data.model.BagRoom
import com.example.turon.data.model.Providers
import com.example.turon.data.model.Qoblar
import com.example.turon.data.model.factory.BagExpenseViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.CreateTinBinding
import com.example.turon.databinding.Expense2DialogBinding
import com.example.turon.databinding.FragmentQoldiqBinding
import com.example.turon.security.viewmodels.BagExpenseViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_bag_expense.*
import kotlinx.coroutines.flow.collect

class QoldiqFragment : Fragment() {
    private var _binding: FragmentQoldiqBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var bagHistoryAdapter: Qoldiq
    private var bagTypeId: Int? = null
    private var bagCount: String = ""
    private var comment: String = ""
    private lateinit var providersList: ArrayList<Providers>
    private lateinit var bagHistoryList: ArrayList<Qoblar>
    private lateinit var bagTypeList: ArrayList<BagRoom>
    private val userId by lazy { SharedPref(requireContext()).getUserId() }
    private lateinit var typeOfTinList: ArrayList<TegirmonData>
    private val viewModel: BagExpenseViewModel by viewModels {
        BagExpenseViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
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
        _binding = FragmentQoldiqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        providersList = ArrayList()
        bagTypeList = ArrayList()
        bagHistoryList = ArrayList()
        typeOfTinList = ArrayList()
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.expenseHistoryRecycler.setHasFixedSize(true)
        getBagHistory()
        initAction()
    }

    private fun getBagHistory() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            val qoldiq = ApiClient.apiService.getQoldiq(userId)
            if (qoldiq.isSuccessful) {
                val qoblar = qoldiq.body()!!.qoblar
                bagHistoryList.clear()
                bagHistoryList.addAll(qoblar)
                bagHistoryAdapter = Qoldiq(bagHistoryList)
                expenseHistoryRecycler.adapter = bagHistoryAdapter
                getBagRoom()
                progressDialog.cancel()
            }
        }
    }

    private fun getBagRoom() {
        lifecycleScope.launchWhenStarted {
            viewModel.getBagRoom(userId)
            viewModel.bagRoomState.collect {
                when (it) {
                    is UIState.Success -> {
                        bagTypeList.clear()
                        bagTypeList.addAll(it.data)
                    }
                }
            }
        }
    }

    private fun getTypeTin() {
        lifecycleScope.launchWhenStarted {
            viewModel.getTypeTin(userId)
            viewModel.typeTinState.collect {
                when (it) {
                    is UIState.Success -> {
                        typeOfTinList.clear()
                        typeOfTinList.addAll(it.data)
                        progressDialog.cancel()
                        showDialog()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initAction() {
        binding.logout.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.inCome -> {
                        findNavController().navigate(R.id.bagIncomeFragment)
                        true
                    }
                    R.id.expense -> {
                        findNavController().navigate(R.id.bagExpenseFragment)
                        true
                    }
                    R.id.qoldiq -> {
                        findNavController().navigate(R.id.qoldiqFragment)
                        true
                    }
                    R.id.create_tin -> {
                        createTin()
                        true
                    }
                    R.id.chiqim_tarixi -> {
                        findNavController().navigate(R.id.kirimBagHistoryFragment)
                        true
                    }
                    R.id.kirim_tarixi -> {
                        findNavController().navigate(R.id.chqimdanQaytarilganlarFragment)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_expense)
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

    private fun createTin() {
        val bind: CreateTinBinding = CreateTinBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Taminotchi yaratish"
        bind.textView35.setOnClickListener {
            var company = bind.company.text.toString()
            var name = bind.name.text.toString()
            var address = bind.address.text.toString()
            var number = bind.number.text.toString()
            var comment = bind.comment.text.toString()
            var debt = bind.debt.text.toString()
            when {
                comment.isEmpty() && company.isEmpty() && name.isEmpty() && address.isEmpty() && number.isEmpty() && debt.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Izoh yozing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    builder.cancel()
                    createTinData(company, name, address, number, comment, debt)
                }
            }
        }
        builder.show()
    }

    private fun createTinData(
        company: String,
        name: String,
        address: String,
        number: String,
        comment: String,
        debt: String
    ) {
        progressDialog.show()
        val map: java.util.HashMap<String, Any> = java.util.HashMap()
        map["compony"] = company
        map["name"] = name
        map["address"] = address
        map["comment"] = comment
        map["phone"] = number
        map["debt"] = debt
        model.crrete_clinet_tin(map).observe(viewLifecycleOwner) {
            if (it.success == true) {
                progressDialog.cancel()
                Toast.makeText(requireContext(), "Yaratildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Xato", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog() {
        val bind: Expense2DialogBinding = Expense2DialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Qop chiqimi"
        Log.e("chiqim_spinnr", "showDialog: $typeOfTinList")
        val adapterProduct = SpinnerCargoManAdapter(requireContext(), typeOfTinList)
        bind.text0.adapter = adapterProduct
        bind.text0.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bagTypeId = typeOfTinList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        bind.textView35.setOnClickListener {
            bagCount = bind.text3.text.toString()
            comment = bind.text4.text.toString()
            when {
                bagCount.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Qop Sonini kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                comment.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Izoh yozing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    builder.cancel()
                    addExpense(bagTypeId, bagCount, comment)
                }
            }
        }
        builder.show()
    }

    private fun addExpense(bagTypeId: Int?, bagCount: String, comment: String) {
        val map: HashMap<String, Any> = HashMap()
        map["user_id"] = userId
        map["qop_id"] = bagTypeId.toString()
        map["soni"] = bagCount
        map["izoh"] = comment
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.addBagExpense(map)
            viewModel.addExpenseState.collect {
                when (it) {
                    is UIState.Success -> {
                        Toast.makeText(requireContext(), "Yaratildi", Toast.LENGTH_SHORT).show()
                        progressDialog.cancel()
                        getBagHistory()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        progressDialog.cancel()
                    }
                    else -> Unit
                }
            }
        }
    }
}