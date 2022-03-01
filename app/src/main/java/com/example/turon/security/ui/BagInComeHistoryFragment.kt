package com.example.turon.security.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.turon.databinding.FragmentBagInComeHistoryBinding
import com.example.turon.security.viewmodels.BagInComeViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
        getTypeTin()
    }

    private fun initAction() {
        binding.txtFrom.text = "Gacha"
        binding.txtUntil.text = "Dan"
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

        binding.oy.setOnClickListener {
            binding.txtFrom.text = "Gacha"
            binding.txtUntil.text = "Dan"
            dateEnd=""
            dateStart=""
            getFilterMethod("", "", "", "oy", "", "")
        }

        binding.kun.setOnClickListener {
            binding.txtFrom.text = "Gacha"
            binding.txtUntil.text = "Dan"
            dateEnd=""
            dateStart=""
            getFilterMethod("", "kun", "", "", "", "")
        }

        binding.hafta.setOnClickListener {
            binding.txtFrom.text = "Gacha"
            binding.txtUntil.text = "Dan"
            dateEnd=""
            dateStart=""
            getFilterMethod("", "", "hafta", "", "", "")
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getTypeTin() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getTypeTin(userId)
            viewModel.typeTinState.collect {
                when (it) {
                    is UIState.Success -> {
                        typeOfTinList.clear()
                        typeOfTinList.addAll(it.data)
                        setupSpinner()
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

    private fun setupSpinner() {
        val adapter = SpinnerCargoManAdapter(requireContext(), typeOfTinList)
        binding.spinnerTin.adapter = adapter
        binding.spinnerTin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(requireContext(), "ttt", Toast.LENGTH_SHORT).show()
                getFilterMethod(typeOfTinList[position].id.toString(), "", "", "", "", "")
                binding.txtFrom.text = "Gacha"
                binding.txtUntil.text = "Dan"
                dateEnd=""
                dateStart=""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

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


    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
            getFilterMethod("", "", "", "", dateStart, dateEnd)
            //getHistoryAktFilter(dateStart, dateEnd)
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanagacha ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        //Toast.makeText(requireContext(), binding.txtFrom.text.toString()+"\n"+dateEnd, Toast.LENGTH_SHORT).show()
    }

    private fun updateDateInViewUntil() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtUntil.text = sdf.format(cal.time)
        dateEnd = binding.txtUntil.text.toString()
        if (dateStart != "") {
            getFilterMethod("", "", "", "", dateStart, dateEnd)
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanadan ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(requireContext(), dateStart + "\n" + dateEnd, Toast.LENGTH_SHORT).show()
    }
}