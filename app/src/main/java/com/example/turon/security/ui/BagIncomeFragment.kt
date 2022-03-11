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
import com.example.turon.adapter.BagExpenseAdapter
import com.example.turon.adapter.SpinnerCargoManAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.factory.BagInComeViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.TegirmonData
import com.example.turon.databinding.ExpenseDialogBinding
import com.example.turon.databinding.FragmentBagIncomeBinding
import com.example.turon.security.viewmodels.BagInComeViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect

class BagIncomeFragment : Fragment() {
    private var _binding: FragmentBagIncomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var bagHistoryAdapter: BagExpenseAdapter
    private var bagTypeId: Int? = null
    private var providerId: Int? = null
    private var bagCount: String = ""
    private var comment: String = ""
    private val userId by lazy { SharedPref(requireContext()).getUserId() }
    private lateinit var providersList: ArrayList<TegirmonData>
    private lateinit var typeOfTinList: ArrayList<TegirmonData>
    private lateinit var bagHistoryList: ArrayList<QopHistory>

    private val viewModel: BagInComeViewModel by viewModels {
        BagInComeViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        providersList = ArrayList()
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
        binding.inComeHistoryRecycler.setHasFixedSize(true)
        getBagHistory()
        initAction()


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
                        findNavController().navigate(R.id.bagInComeHistoryFragment)
                        Toast.makeText(requireContext(), "turns", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.expense -> {
                        findNavController().navigate(R.id.bagExpenseFragment)

                        true
                    }
                    R.id.qoldiq -> {
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

