package com.example.turon.scales.ui.product_acceptance

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
import com.example.turon.R
import com.example.turon.adapter.ProductListAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.AcceptProduct
import com.example.turon.data.model.factory.AcceptDetailsViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentAcceptanceDetails2Binding
import com.example.turon.auth.AuthActivity
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class AcceptanceDetailsFragment : Fragment() {
    private var _binding: FragmentAcceptanceDetails2Binding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductListAdapter
    private lateinit var list: ArrayList<AcceptProduct>
    private val viewModel: AcceptDetailsViewModel by viewModels {
        AcceptDetailsViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
    private var id: String? = null
    private var aktName: String? = null
    private var clientName: String? = null
    private var wagonCount: String? = null
    private var stansiya: String? = null
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id")
            aktName = it.getString("aktName")
            clientName = it.getString("clientName")
            wagonCount = it.getString("wagonCount")
            stansiya = it.getString("stansiya")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAcceptanceDetails2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initAction()
    }

    private fun initAction() {
        binding.logout.setOnClickListener {
            val popupMenu= PopupMenu(requireContext(),it)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.out->{
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                        requireActivity().finishAffinity()
                        true
                    }
                    else->false
                }
            }
            popupMenu.inflate(R.menu.option_menu)
            try {
                val fieldMPopup= PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible=true
                val mPopup=fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                    .invoke(mPopup,true)
            }catch (e: Exception){
                Log.d("TAG","Error show menu icon")
            }finally {
                popupMenu.show()
            }
        }
    }

    private fun initView() {
        binding.textView6.text = aktName
        binding.textView7.text = stansiya
        binding.textView8.text = clientName
        binding.textView21.text = wagonCount
        list = ArrayList()

        lifecycleScope.launchWhenStarted {
            val map = HashMap<String, Any>()
            map["akt_id"] = "2"
            map["vagon_raqami"] = "201010"
//            map["brutto"] = binding.bruttoEdit.text.toString()
//            map["tara"] = binding.taraEdit.text.toString()?:"0"
            viewModel.addWagon(map)
            viewModel.addWagonState.collect {
                when (it) {

                    is UIState.Success -> {
                        binding.textView26.text=it.data.total.vagon_soni.toString()
                        binding.textView27.text=it.data.total.totalBrutto.toString()
                        binding.textView28.text=it.data.total.totalTara.toString()
                        binding.textView29.text=it.data.total.totalNeto.toString()
                    }
                    is UIState.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UIState.Loading, UIState.Empty -> Unit
                }
            }


        }




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


}