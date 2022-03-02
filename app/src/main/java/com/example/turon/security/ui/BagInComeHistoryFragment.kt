package com.example.turon.security.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.turon.adapter.BagExpenseAdapter
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.InComeRequest
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.factory.BagInComeViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.ExpenseDialogBinding
import com.example.turon.databinding.FragmentBagInComeHistoryBinding
import com.example.turon.security.viewmodels.BagInComeViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*


class BagInComeHistoryFragment : Fragment() {
    private var _binding: FragmentBagInComeHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagInComeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

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

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()

        initAction()
        binding.inComeHistoryRecycler.setHasFixedSize(true)
        getBagHistory()

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
                        bagHistoryAdapter = BagExpenseAdapter(bagHistoryList)
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
                    R.id.history -> {
                        findNavController().navigate(R.id.bagInComeHistoryFragment)
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

    private fun getBagHistory() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getBagHistory(userId)
            viewModel.bagHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        bagHistoryList.clear()
                        bagHistoryList.addAll(it.data)
                        bagHistoryAdapter = BagExpenseAdapter(bagHistoryList)
                        binding.inComeHistoryRecycler.adapter = bagHistoryAdapter
                        getProviders()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }

        }
    }


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

    private fun addOfTin(bagTypeId: Int?, providerId: Int?, count: String, comment: String) {
        progressDialog.show()
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
                        getBagHistory()
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