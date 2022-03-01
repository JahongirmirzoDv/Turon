package com.example.turon.production.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.ProductHistoryFilterAdapter
import com.example.turon.adapter.ProductionHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentAcceptHistoryProBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class AcceptHistoryProFragment : Fragment() {
    private var _binding: FragmentAcceptHistoryProBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterHistory: ProductionHistoryAdapter
    private val list by lazy { ArrayList<HistoryProData>() }
    private lateinit var progressDialog: AlertDialog
    private lateinit var list_history: ArrayList<HistoryProData>
    private val sharedPref by lazy { SharedPref(requireContext()) }
    private var dateStart: String = ""
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener


    private val viewModel: AllHistoryViewModel by viewModels {
        AllHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java, requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("id", "onCreate: ${sharedPref.getUserId()}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcceptHistoryProBinding.inflate(inflater, container, false)

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
        binding.recyclerHistoryPro.setHasFixedSize(true)
        list_history = ArrayList()
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
        setRecycler()
        getHistoryProductFilter(start_date,date1)


        binding.txtFrom.text = "Dan"
        binding.txtUntil.text = "Gacha"
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

    private fun getHistoryProductFilter(dateStart: String, dateEnd: String) {
        progressDialog.show()
        var dd = ProductHistoryFilterAdapter()
        lifecycleScope.launchWhenStarted {
            viewModel.getHistoryProFilter(sharedPref.getUserId(), dateStart, dateEnd)
                .observe(viewLifecycleOwner) {
                    Log.e(
                        "Lists",
                        "getHistoryProductFilter: ${it.count}  ${it.results}",
                    )
                    if (it.results.isNotEmpty()) {
                        dd.lists = it.results
                        binding.recyclerHistoryPro.adapter = dd
                        progressDialog.dismiss()
                    } else {
                        dd.lists = emptyList()
                        progressDialog.dismiss()
                    }
                }
        }
    }

    private fun setRecycler() {
        adapterHistory = ProductionHistoryAdapter()
        binding.recyclerHistoryPro.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterHistory.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { adapterHistory.retry() },
                footer = AdvertLoadStateAdapter { adapterHistory.retry() }
            )
        }

        adapterHistory.addLoadStateListener { loadStates ->
            when (loadStates.refresh) {
                is LoadState.NotLoading -> {
                    progressDialog.dismiss()
                }
                is LoadState.Loading -> {
                    progressDialog.show()
                }
                is LoadState.Error -> {
                    progressDialog.dismiss()
                }
            }
        }
    }


    private fun getHistoryPro() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getProductionPagination()
            response.collect {
                adapterHistory.submitData(it)
            }
        }
    }
}