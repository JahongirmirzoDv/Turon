package com.example.turon.feed.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turon.R
import com.example.turon.adapter.AdvertLoadStateAdapter
import com.example.turon.adapter.SendOrderHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Result
import com.example.turon.data.model.factory.AllHistoryViewModelFactory
import com.example.turon.databinding.FragmentFeedSendHistoryBinding
import com.example.turon.production.viewmodels.AllHistoryViewModel
import com.example.turon.utils.SharedPref
import com.example.turon.utils.textChanges
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.toolbar_default.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FeedSendHistoryFragment : Fragment() {
    private var dateStart: String = ""
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener
    private var _binding: FragmentFeedSendHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private val orderHistoryAdapter by lazy { SendOrderHistoryAdapter() }
    private val orderList by lazy { ArrayList<Result>() }

    private val viewModel: AllHistoryViewModel by viewModels {
        AllHistoryViewModelFactory(
            ApiClient.createService(ApiService::class.java, requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedSendHistoryBinding.inflate(inflater, container, false)
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
        setupUI()
        initAction()

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
            var c = LocalDate.now()
            val minusMonths = c.minusDays(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getAcceptHistory(text = "", start_date, date1)
        }
        binding.hafta.setOnClickListener {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = df.format(Calendar.getInstance().time)
            var c = LocalDate.now()
            val minusMonths = c.minusWeeks(1)
            val mont = String.format("%02d", minusMonths.monthValue)
            val day = String.format("%02d", minusMonths.dayOfMonth)
            var start_date = "${minusMonths.year}-$mont-$day"
            getAcceptHistory(text = "", start_date, date1)
        }
    }

    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
            getAcceptHistory(text = "", dateEnd, dateStart)
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
            getAcceptHistory(text = "", dateEnd, dateStart)
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
    private fun searchOrderHistory() {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date1 = df.format(Calendar.getInstance().time)
        var c = LocalDate.now()
        val minusMonths = c.minusMonths(1)
        val mont = String.format("%02d", minusMonths.monthValue)
        val day = String.format("%02d", minusMonths.dayOfMonth)
        var start_date = "${minusMonths.year}-$mont-$day"
        binding.toolbarSearch.etSearch.requestFocus()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val editTextFlow = binding.toolbarSearch.etSearch.textChanges()
            editTextFlow
                .debounce(1000)
                .onEach {
                    getAcceptHistory(it.toString(), start_date, date1)
                }.launchIn(this)

        }

    }


    private fun hideShowSearch() {
        with(binding) {
            toolbarDefault.search.setOnClickListener {
                val toolbarD: View = toolbarDefault.root
                val toolbarS: View = toolbarSearch.root
                toolbarD.isVisible = false
                toolbarS.isVisible = true
                binding.toolbarSearch.etSearch.requestFocus()
            }
            toolbarSearch.ivBack.setOnClickListener {
                val toolbarD: View = toolbarDefault.root
                val toolbarS: View = toolbarSearch.root
                toolbarD.isVisible = true
                toolbarS.isVisible = false
            }
            toolbarSearch.ivClear.setOnClickListener {
                toolbarSearch.etSearch.setText("")
            }
            toolbarSearch.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    toolbarSearch.ivClear.isVisible = !s.isNullOrEmpty()
//                    toolbarSearch.ivClear.isVisible = s != ""
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.recyclerOrder.setHasFixedSize(true)
        binding.appBarLayout.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setRecycler()
        hideShowSearch()
        searchOrderHistory()


    }

    private fun setRecycler() {
        binding.recyclerOrder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderHistoryAdapter.withLoadStateHeaderAndFooter(
                header = AdvertLoadStateAdapter { orderHistoryAdapter.retry() },
                footer = AdvertLoadStateAdapter { orderHistoryAdapter.retry() }
            )
        }
        orderHistoryAdapter.addLoadStateListener { loadStates ->

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

        orderHistoryAdapter.setOnClickListener(object :
            SendOrderHistoryAdapter.OnParcelClickListener {
            @SuppressLint("SetTextI18n")
            override fun clickListener(parcel: Result) {
                val bind: com.example.turon.databinding.InfoBinding =
                    com.example.turon.databinding.InfoBinding.inflate(layoutInflater)
                val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                dialog.setCancelable(true)
                dialog.setView(bind.root)
                val builder = dialog.create()
                var list = ArrayList<String>()
                parcel.baskets.forEach {
                    list.add(
                        "\n ${it.productN.productX.name} : ${
                            it.hajmi.toString().substring(0, it.hajmi.toString().length - 2)
                        }"
                    )
                }
                bind.dialogTitle.text = parcel.customer.name
                bind.location.text =
                    "Sotuvchi : ${parcel.seller.first_name} ${parcel.seller.last_name}"
                bind.limit.text = "Qoplar : $list"
                Picasso.get().load(parcel.img).error(R.drawable.no_photo)
                    .placeholder(R.drawable.no_photo).into(bind.img)
                bind.textView35.setOnClickListener {
                    builder.cancel()
                }
                builder.show()
            }
        })
    }

    private fun getAcceptHistory(text: String, dateStart: String, dateEnd: String) {
        val userId = SharedPref(requireContext()).getUserId()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val response = viewModel.getOrderPagination(text, userId, dateStart, dateEnd)
            response.collect {
                try {
                    orderHistoryAdapter.submitData(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}