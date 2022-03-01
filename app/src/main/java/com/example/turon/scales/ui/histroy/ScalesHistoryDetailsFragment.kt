package com.example.turon.scales.ui.histroy

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.example.turon.R
import com.example.turon.adapter.OnEditListenerT
import com.example.turon.adapter.ProductAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.factory.ScalesHistoryViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AcceptDetailsWagon
import com.example.turon.databinding.FragmentScalesHistoryDetailsBinding
import com.example.turon.auth.AuthActivity
import com.example.turon.data.model.EditScales
import com.example.turon.data.model.RequestEditScales
import com.example.turon.utils.Utils
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class ScalesHistoryDetailsFragment : Fragment(), OnEditListenerT {
    private var _binding: FragmentScalesHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private lateinit var progressDialog: AlertDialog
    private lateinit var list: ArrayList<AcceptDetailsWagon>
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val editList by lazy { ArrayList<EditScales>() }

    private val viewModel: ScalesHistoryViewModel by viewModels {
        ScalesHistoryViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    private var id: String? = null
    private var aktName: String? = null
    private var clientName: String? = null
    private var wagonCount: String? = null
    private var stansiya: String? = null
    private var title: String? = null
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            id = it.getString("id")
            aktName = it.getString("aktName")
            clientName = it.getString("clientName")
            wagonCount = it.getString("wagonCount")
            stansiya = it.getString("stansiya")
            title = it.getString("title")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScalesHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarTitle.text = title
        initView()
        initAction()
        getAktWagonAll()

    }

    private fun getAktWagonAll() {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getAktWagonAll(id.toString())
            viewModel.aktWagonAllState.collect {
                when (it) {
                    is UIState.Success -> {
                        adapter = ProductAdapter(this@ScalesHistoryDetailsFragment)
                        list.addAll(it.data.wagons)
                        adapter.submitList(it.data.wagons)
                        binding.recyclerProduct.adapter = adapter
                        binding.textView26.text = it.data.total.vagon_soni.toString()
                        binding.textView27.text = it.data.total.totalBrutto.toString()
                        binding.textView28.text = it.data.total.totalTara.toString()
                        binding.textView29.text = it.data.total.totalNeto.toString()
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


    private fun initAction() {
        binding.btnSave.setOnClickListener {
            showDialog()
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

    private fun initView() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        binding.textView6.text = aktName
        binding.textView7.text = stansiya
        binding.textView8.text = clientName
        binding.textView21.text = wagonCount
        list = ArrayList()
        binding.recyclerProduct.setHasFixedSize(true)

    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.item_dialog)
        val body = dialog.findViewById(R.id.textView30) as TextView
        val yesBtn = dialog.findViewById(R.id.button3) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            requireActivity().onBackPressed()
        }

        dialog.show()
    }


    private fun editAktHistoryFun(aktId: Int) {
        //progressDialog.show()

        lifecycleScope.launchWhenStarted {
            viewModel.editAktHistory(RequestEditScales(aktId, list))
            viewModel.editAktHistoryState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        //Utils.closeKeyboard(requireActivity())
                        binding.textView26.text = it.data.total.vagon_soni.toString()
                        binding.textView27.text = it.data.total.totalBrutto.toString()
                        binding.textView28.text = it.data.total.totalTara.toString()
                        binding.textView29.text = it.data.total.totalNeto.toString()
//                        getAktWagonAll()
                        //showDialog()
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()

                    }
                    is UIState.Loading -> {
                        progressDialog.show()
                    }

                }
            }

        }


    }

    override fun onItemEditListener(position: Int, wagonId: Int, brutto: Int, tara: Int) {
        list[position].bruttoFakt = brutto
        list[position].taraFakt = tara
        val a = list.filter { it.bruttoFakt == 0 }.size
        val b = list.filter { it.taraFakt == 0 }.size
        if(a+b==0) editAktHistoryFun(id!!.toInt())
    }


}