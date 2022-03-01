package com.example.turon.scales.ui.histroy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.HistoryAdapter
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.factory.ProductAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData
import com.example.turon.databinding.FragmentScalesHistoryBinding
import com.example.turon.scales.ui.product_acceptance.ProductAcceptViewModel
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*


class ScalesHistoryFragment : Fragment(), HistoryAdapter.OnHistoryListener {
    private var _binding: FragmentScalesHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var dateSetListenerFrom: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerUntil: DatePickerDialog.OnDateSetListener
    private lateinit var list: ArrayList<ProductAcceptData>
    private var dateStart: String = ""
    private var dateEnd: String = ""
    var cal: Calendar = Calendar.getInstance()
    private val viewModel: ProductAcceptViewModel by viewModels {
        ProductAcceptViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
    private lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScalesHistoryBinding.inflate(inflater, container, false)
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
        binding.recyclerHistory.setHasFixedSize(true)
        list = ArrayList()
        //getHistoryAkt()
        setupUI()
        progressDialog.show()

    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()

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
        binding.logout.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.out -> {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finishAffinity()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu)
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

    private fun getHistoryAkt() {
        lifecycleScope.launchWhenStarted {
            viewModel.getHistoryAkt()
            viewModel.aktHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        list.clear()
                        list.addAll(it.data)
                        adapter = HistoryAdapter(
                            this@ScalesHistoryFragment,
                            requireContext(),
                            list
                        )
                        binding.recyclerHistory.adapter = adapter
                        progressDialog.dismiss()
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


    private fun getHistoryAktFilter(dateStart: String, dateEnd: String) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getHistoryAktFilter(dateStart, dateEnd)
            viewModel.aktHistoryStateFilter.collect {
                when (it) {
                    is UIState.Success -> {
                        list.clear()
                        list.addAll(it.data)

                        adapter = HistoryAdapter(
                            this@ScalesHistoryFragment,
                            requireContext(),
                            list
                        )
                        binding.recyclerHistory.adapter = adapter
                        progressDialog.dismiss()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "xato", Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading, UIState.Empty -> Unit

                }
            }

        }
    }


    override fun onItemHistoryListener(position: Int, productAcceptData: ProductAcceptData) {
        val bundle = bundleOf(
            "id" to productAcceptData.id.toString(),
            "aktName" to productAcceptData.name,
            "clientName" to productAcceptData.client.compony,
            "wagonCount" to productAcceptData.vagonSoni.toString(),
            "stansiya" to productAcceptData.stansiya,
            "title" to "Qabul tarixi"

        )
        findNavController().navigate(
            R.id.action_scalesHistoryFragment_to_scalesHistoryDetailsFragment,
            bundle
        )
    }

    private fun updateDateInViewFrom() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.txtFrom.text = sdf.format(cal.time)
        dateStart = binding.txtFrom.text.toString()
        if (dateEnd != "") {
            getHistoryAktFilter(dateStart, dateEnd)
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
            getHistoryAktFilter(dateStart, dateEnd)
            Log.e("DAta", "updateDateInViewUntil: $dateStart$dateEnd")
        } else {
            Toast.makeText(
                requireContext(),
                "Qaysi sanadan ekanligini kiriting",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(requireContext(), dateStart + "\n" + dateEnd, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()
        getHistoryAkt()
    }


}