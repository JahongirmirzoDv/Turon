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
import com.example.turon.adapter.BagInComeAdapter
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.BagExpenseHistory
import com.example.turon.data.model.BagRoom
import com.example.turon.data.model.Providers
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

class BagExpenseFragment : Fragment() {
    private var _binding: FragmentBagExpenseBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var bagHistoryAdapter: BagInComeAdapter
    private var bagTypeId: Int? = null
    private var bagCount: String = ""
    private var comment: String = ""
    private lateinit var providersList: ArrayList<Providers>
    private lateinit var bagHistoryList: ArrayList<BagExpenseHistory>
    private lateinit var bagTypeList: ArrayList<BagRoom>
    private val userId by lazy { SharedPref(requireContext()).getUserId() }
    private lateinit var typeOfTinList: ArrayList<TegirmonData>
    private val viewModel: BagExpenseViewModel by viewModels {
        BagExpenseViewModelFactory(
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
        _binding = FragmentBagExpenseBinding.inflate(inflater, container, false)
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
            viewModel.getBagHistory(userId)
            viewModel.bagHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        bagHistoryList.clear()
                        bagHistoryList.addAll(it.data)
                        bagHistoryAdapter = BagInComeAdapter(bagHistoryList)
                        expenseHistoryRecycler.adapter = bagHistoryAdapter
                        getBagRoom()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
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
                    R.id.qoldiq->{
                        findNavController().navigate(R.id.qoldiqFragment)
                        Toast.makeText(requireContext(), "turns", Toast.LENGTH_SHORT).show()
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
            } catch (e: Exception) {
                Log.d("TAG", "Error show menu icon")
            } finally {
                popupMenu.show()
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
        Log.e("chiqim_spinnr", "showDialog: ${typeOfTinList.toString()}")
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
                when (it) {
                    is UIState.Success -> {
                        Toast.makeText(requireContext(), "Yaratildi", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        getBagHistory()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }

                    else -> Unit
                }

            }

        }
    }


}