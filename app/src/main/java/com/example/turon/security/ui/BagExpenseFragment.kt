package com.example.turon.security.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.BagInComeAdapter
import com.example.turon.adapter.ChiqimAdapter
import com.example.turon.adapter.ProductHistoryFilterAdapter
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
import com.example.turon.data.model.QopChiqim
import com.example.turon.data.model.factory.BagExpenseViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.Expense2DialogBinding
import com.example.turon.databinding.FragmentBagExpenseBinding
import com.example.turon.security.viewmodels.BagExpenseViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_bag_expense.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class BagExpenseFragment : Fragment() {
    private var _binding: FragmentBagExpenseBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var bagHistoryAdapter: BagInComeAdapter
    private var bagTypeId: Int? = null
    private var bagCount: String = ""
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private var comment: String = ""
    private var dateStart: String = ""
    private var providerId: Int? = null
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener
    private lateinit var providersList: ArrayList<Providers>
    private lateinit var bagHistoryList: ArrayList<QopChiqim>
    private lateinit var bagTypeList: ArrayList<BagRoom>
    private val userId by lazy { SharedPref(requireContext()).getUserId() }
    lateinit var apiService: ApiService
    private lateinit var typeOfTinList: ArrayList<TegirmonData>
    private val viewModel: BagExpenseViewModel by viewModels {
        BagExpenseViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
    private val model: ControlViewModel by viewModels {
        ViewModelFactory(ApiHelper2(ApiClient2.createService(ApiService2::class.java,requireContext())))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagExpenseBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateSetListenerFrom =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewFrom()
            }
        dateSetListenerUntil =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewUntil()
            }
        providersList = ArrayList()
        bagTypeList = ArrayList()
        bagHistoryList = ArrayList()
        typeOfTinList = ArrayList()
        setupUI()

    }

    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
            getHistoryProductFilter(dateStart, dateEnd)
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanagacha ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(
            requireContext(),
            binding.txtFrom.text.toString() + "\n" + dateEnd,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateDateInViewUntil() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtUntil.text = sdf.format(cal.time)
        dateEnd = binding.txtUntil.text.toString()
        if (dateStart != "") {
            getHistoryProductFilter(dateStart, dateEnd)
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanadan ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(requireContext(), dateStart + "\n" + dateEnd, Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date1 = df.format(Calendar.getInstance().time)
        var c = LocalDate.now()
        val minusMonths = c.minusMonths(1)
        val mont = String.format("%02d", minusMonths.monthValue)
        val day = String.format("%02d", minusMonths.dayOfMonth)
        var start_date = "${minusMonths.year}-$mont-$day"
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.expenseHistoryRecycler.setHasFixedSize(true)
        getHistoryProductFilter(start_date,date1)
        initAction()


    }

    private fun getHistoryProductFilter(dateStart: String, dateEnd: String) {
        progressDialog.show()
        var dd = ChiqimAdapter()
        lifecycleScope.launchWhenStarted {
            model.getQop(sharedPref.getUserId(), dateStart, dateEnd)
                .observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        bagHistoryList.clear()
                        bagHistoryList.addAll(it)
                        dd.list = bagHistoryList
                        binding.expenseHistoryRecycler.adapter = dd
                        getBagRoom()
                    } else {
                        dd.list = emptyList()
                        progressDialog.dismiss()
                    }
                }
        }
    }

    private fun getBagRoom() {
        lifecycleScope.launchWhenStarted {
            viewModel.getBagRoom(userId)
            viewModel.bagRoomState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
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
                        progressDialog.dismiss()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAction() {
        binding.addBag.setOnClickListener {
            getTypeTin()
        }
        binding.logout.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.inCome -> {
                        findNavController().navigate(R.id.bagIncomeFragment)
                        true
                    }
                    R.id.expense->{
                        findNavController().navigate(R.id.bagExpenseFragment)
                        true
                    }
                    R.id.qoldiq->{
                        findNavController().navigate(R.id.qoldiqFragment)
                        Toast.makeText(requireContext(), "turns", Toast.LENGTH_SHORT).show()
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
        binding.txtUntil.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListenerUntil,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.txtFrom.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListenerFrom,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.kun.setOnClickListener {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = df.format(Calendar.getInstance().time)
            var c = LocalDate.now()
            val minusMonths = c.minusDays(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getHistoryProductFilter(start_date,date1)
        }
        binding.hafta.setOnClickListener {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = df.format(Calendar.getInstance().time)
            var c = LocalDate.now()
            val minusMonths = c.minusWeeks(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getHistoryProductFilter(start_date,date1)
        }
    }

    private fun showDialog() {
        val bind: Expense2DialogBinding = Expense2DialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Qop chiqimi"
        val adapterProduct = SpinnerCargoManAdapter(requireContext(), typeOfTinList)
        bind.text0.adapter = adapterProduct
        bind.text0.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bagTypeId = bagTypeList[position].id


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
                    builder.dismiss()
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
                when(it){
                    is UIState.Success->{
                        Toast.makeText(requireContext(), "Yaratildi", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    is UIState.Error->{
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }

        }
    }

}