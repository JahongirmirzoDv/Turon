package com.example.turon.security.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.TurnPagingAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.factory.TurnHistoryViewModelFactory
import com.example.turon.databinding.TurnHistoryFragmentBinding
import com.example.turon.security.viewmodels.TurnHistoryViewModel
import com.example.turon.utils.textChanges
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TurnHistoryFragment : Fragment() {
    private var _binding: TurnHistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val orderAdapter by lazy { TurnPagingAdapter() }
    private lateinit var orderList: ArrayList<TurnHistory>
    private var status = true
    lateinit var current_date: String
    lateinit var date1: String
    lateinit var list: ArrayList<TurnHistory>
    private var dateStart: String = ""
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener


    private val viewModel: TurnHistoryViewModel by viewModels {
        TurnHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java, requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TurnHistoryFragmentBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
            getTurnPaginationTo("", dateEnd, dateStart)
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
            getTurnPaginationTo("", dateEnd, dateStart)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderList = ArrayList()
        list = ArrayList()
        setupUI()
        initAction()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        date1 = df.format(Calendar.getInstance().time)
        var c = LocalDate.now()
        val minusMonths = c.minusMonths(1)
        val mont = String.format("%02d", minusMonths.monthValue)
        val day = String.format("%02d", minusMonths.dayOfMonth)
        current_date = "${minusMonths.year}-$mont-$day"


        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        setRecyclerTo()
        setRecyclerVi()
        searchOrderHistory(current_date,date1)
    }

    private fun searchOrderHistory(current_date1: String, date1: String) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val editTextFlow = binding.searchView.textChanges()
            editTextFlow
                .debounce(1000)
                .onEach {
                    if (status) getTurnPaginationTo(it.toString(),current_date1,date1)
                    else getHistoryTurn(it.toString())
                }.launchIn(this)
        }
    }

    private fun setRecyclerTo() {
        binding.recyclerTo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderAdapter.retry() }
            )
        }

        orderAdapter.addLoadStateListener { loadStates ->

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

        orderAdapter.setOnClickListener(object : TurnPagingAdapter.OnParcelClickListener {
            override fun clickListener(parcel: TurnHistory) {

            }
        })
    }

    private fun setRecyclerVi() {
        binding.recyclerVi.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderAdapter.retry() }
            )
        }

        orderAdapter.addLoadStateListener { loadStates ->
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

        orderAdapter.setOnClickListener(object : TurnPagingAdapter.OnParcelClickListener {
            override fun clickListener(parcel: TurnHistory) {

            }
        })
    }

    private fun getHistoryTurn(text: String) {
        binding.recyclerVi.isVisible = true
        binding.recyclerTo.isVisible = false
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getTurnPagination(text)
            response.collect {
                orderAdapter.submitData(it)
            }
        }
    }


    private fun getTurnPaginationTo(text: String,from_date: String, to_date: String) {
        binding.recyclerVi.isVisible = false
        binding.recyclerTo.isVisible = true
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getTurnPaginationTo(text,from_date,to_date)
            response.collect {
                orderAdapter.submitData(it)
                it.map { ti ->
                    list.add(ti)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAction() {

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
            val c = LocalDate.now()
            val minusMonths = c
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            val start_date = "${minusMonths.year}-$mont-$day"
            getTurnPaginationTo("", start_date, date1)
        }
        binding.hafta.setOnClickListener {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = df.format(Calendar.getInstance().time)
            val c = LocalDate.now()
            val minusMonths = c.minusWeeks(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            val start_date = "${minusMonths.year}-$mont-$day"
            getTurnPaginationTo("", start_date, date1)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShahar.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#FFCC66"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#E4D2B6"))
            status = true
            getTurnPaginationTo("",current_date,date1)
            // status = 1
        }
        binding.btnViloyat.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#E4D2B6"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#FFCC66"))
            status = false
            getHistoryTurn("")
//            status = 0
        }
    }
}