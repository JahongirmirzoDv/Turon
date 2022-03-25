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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.BagExpenseAdapter
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.data.model.InComeRequest
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.factory.BagInComeViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.BagExpenceSecurityBinding
import com.example.turon.databinding.CreateTinBinding
import com.example.turon.databinding.ExpenseDialogBinding
import com.example.turon.databinding.FragmentBagInComeHistoryBinding
import com.example.turon.security.viewmodels.BagInComeViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class BagInComeHistoryFragment : Fragment() {
    private var _binding: FragmentBagInComeHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private var bagCount: String = ""
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private var comment: String = ""
    private lateinit var bagHistoryAdapter: BagExpenseAdapter
    private var bagTypeId: Int? = null
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener
    private lateinit var list: ArrayList<ProductAcceptData>
    private var dateStart: String = ""
    private var providerId: Int? = null
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private val userId by lazy { SharedPref(requireContext()).getUserId() }
    private lateinit var providersList: ArrayList<TegirmonData>
    private lateinit var typeOfTinList: ArrayList<TegirmonData>
    private lateinit var bagHistoryList: ArrayList<QopHistory>

    private val viewModel: BagInComeViewModel by viewModels {
        BagInComeViewModelFactory(
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
        _binding = FragmentBagInComeHistoryBinding.inflate(inflater, container, false)
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
        bagHistoryList = ArrayList()
        providersList = ArrayList()
        typeOfTinList = ArrayList()
        setupUI()
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

        initAction()
        binding.inComeHistoryRecycler.setHasFixedSize(true)
        getHistoryProductFilter(start_date, date1)
    }


    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
//            getHistoryProductFilter(dateStart, dateEnd)
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
//            getHistoryProductFilter(dateStart, dateEnd)
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanadan ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(requireContext(), dateStart + "\n" + dateEnd, Toast.LENGTH_SHORT).show()
    }

    private fun getFilterMethod(
        id: String,
        kun: String,
        hafta: String,
        oy: String,
        start: String,
        end: String
    ) {
        val request = InComeRequest(
            id,
            kun,
            hafta,
            oy,
            start,
            end,
            SharedPref(requireContext()).getUserId()
        )

        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getFilterTin(request)
            viewModel.filterTinState.collect {
                when (it) {
                    is UIState.Success -> {
                        bagHistoryList.clear()
                        bagHistoryList.addAll(it.data)
                        bagHistoryAdapter =
                            BagExpenseAdapter(bagHistoryList, object : BagExpenseAdapter.onPress {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun click(data: QopHistory, position: Int) {
                                    getTypeTin(true)
                                }
                            })
                        binding.inComeHistoryRecycler.adapter = bagHistoryAdapter
                        progressDialog.dismiss()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
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
                    R.id.kirim_tarixi->{
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
            } catch (e: java.lang.Exception) {
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
            val minusMonths = c
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getHistoryProductFilter(start_date, date1)
        }
        binding.hafta.setOnClickListener {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = df.format(Calendar.getInstance().time)
            var c = LocalDate.now()
            val minusMonths = c.minusWeeks(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getHistoryProductFilter(start_date, date1)
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
            val company = bind.company.text.toString()
            val name = bind.name.text.toString()
            val address = bind.address.text.toString()
            val number = bind.number.text.toString()
            val comment = bind.comment.text.toString()
            val debt = bind.debt.text.toString()
            when {
                comment.isEmpty() && company.isEmpty() && name.isEmpty() && address.isEmpty() && number.isEmpty() && debt.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Izoh yozing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    builder.dismiss()
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
        val map: HashMap<String, Any> = HashMap()
        map["compony"] = company
        map["name"] = name
        map["address"] = address
        map["comment"] = comment
        map["phone"] = number
        map["debt"] = debt
        model.crrete_clinet_tin(map).observe(viewLifecycleOwner) {
            if (it.success == true) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Yaratildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Xato", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getProviders() {
        lifecycleScope.launchWhenStarted {
            viewModel.getProvider(userId)
            viewModel.providerState.collect {
                when (it) {
                    is UIState.Success -> {
                        providersList.clear()
                        providersList.addAll(it.data)
                        progressDialog.dismiss()
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
    private fun getTypeTin(ish: Boolean = false) {
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

    private fun showDialog2(data: QopHistory, position: Int) {
        val bind: BagExpenceSecurityBinding = BagExpenceSecurityBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Qop qaytarish"
        bind.text0.text = data.type.name
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
                    Log.e("quantity", "showDialog2: $bagCount - ${data.quantity}")
                    if (bagCount.toInt() <= data.quantity) {
                        Log.e("quantity", "showDialog2: true")
                        builder.dismiss()
                        addExpense(data.type.id, bagCount, comment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Noto'gri miqdor kiritdingiz",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        builder.show()
    }

    private fun addExpense(bagTypeId: Int?, bagCount: String, comment: String) {
        val map: HashMap<String, Any> = HashMap()
        map["income_qop_type_id"] = bagTypeId!!
        map["user_id"] = sharedPref.getUserId()
        map["soni"] = bagCount
        map["izoh"] = comment
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            model.returnIncomeQop(map).observe(viewLifecycleOwner) {
                if (it.success == true) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Qaytarildi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun getHistoryProductFilter(dateStart: String, dateEnd: String) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            model.getBagHistory(userId,dateStart,dateEnd).observe(viewLifecycleOwner) {
                if (it.success) {
                    progressDialog.cancel()
                    bagHistoryList.clear()
                    bagHistoryList.addAll(it.qop_history)
                    bagHistoryAdapter =
                        BagExpenseAdapter(bagHistoryList, object : BagExpenseAdapter.onPress {
                            override fun click(data: QopHistory, position: Int) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    showDialog2(data, position)
                                }
                            }
                        })
                    binding.inComeHistoryRecycler.adapter = bagHistoryAdapter
                    getProviders()
                } else {
                    progressDialog.cancel()
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog() {
        val bind: ExpenseDialogBinding = ExpenseDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(true)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.dialogTitle.text = "Qop kirimi"
        bind.comp.text = "Taminotchi"
        val adapterProduct = SpinnerCargoManAdapter(requireContext(), typeOfTinList)
        bind.text0.adapter = adapterProduct
        val adapterProvider = SpinnerCargoManAdapter(requireContext(), providersList)
        bind.text1.adapter = adapterProvider
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

        bind.text1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                providerId = providersList[position].id

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        bind.textView35.setOnClickListener {
            val count = bind.text3.text.toString()
            val comment = bind.text4.text.toString()
            addOfTin(bagTypeId, providerId, count, comment)
            builder.dismiss()
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addOfTin(bagTypeId: Int?, providerId: Int?, count: String, comment: String) {
        progressDialog.show()
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date1 = df.format(Calendar.getInstance().time)
        var c = LocalDate.now()
        val minusMonths = c.minusMonths(1)
        val mont = String.format("%02d", minusMonths.monthValue)
        val day = String.format("%02d", minusMonths.dayOfMonth)
        var start_date = "${minusMonths.year}-$mont-$day"
        lifecycleScope.launchWhenStarted {
            val map: HashMap<String, Any> = HashMap()
            map["user_id"] = userId
            map["taminotchi"] = providerId!!
            map["type"] = bagTypeId!!
            map["soni"] = count
            map["izoh"] = comment
            viewModel.addBagInCome(map)
            viewModel.addBagInComeState.collect {
                when (it) {
                    is UIState.Success -> {
                        Toast.makeText(requireContext(), "Bajarildi", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        getHistoryProductFilter(start_date, date1)
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}