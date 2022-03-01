package com.example.turon.feed.sendproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.turon.R
import com.example.turon.adapter.SpinnerCompanyAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.SpinnerForProduct
import com.example.turon.data.model.factory.FeedSendViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.FragmentSendOrderFinalBinding
import com.example.turon.utils.FileUriUtils
import com.example.turon.utils.SharedPref
import com.example.turon.utils.compress2
import com.github.drjacky.imagepicker.ImagePicker
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

class SendOrderFinalFragment : Fragment() {
    private var _binding: FragmentSendOrderFinalBinding? = null
    private val binding get() = _binding!!
    private var mCameraUri: Uri? = null
    private val listCargo by lazy { ArrayList<SpinnerForProduct>() }
    private var orderId: Int? = null
//    private var cargoId: Int? = null
    private var filePath: String? = null
    private lateinit var sharedPref: SharedPref
    private val viewModel: SendProductViewModel by viewModels {
        FeedSendViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mCameraUri = uri
                filePath = FileUriUtils.getRealPath(requireActivity(), uri)
                if (filePath!=null){
                    binding.carImag2.visibility=View.GONE
                    binding.carImage.visibility=View.VISIBLE
                    binding.carImage.setImageURI(Uri.fromFile(File(filePath!!)))

                }

            } else parseError(it)
        }

    private lateinit var progressDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = requireArguments().getInt("orderId")


    }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(
                requireContext(),
                ImagePicker.getError(activityResult.data),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendOrderFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        initAction()
        setupUI()
        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        val data = sharedPref.getClientData()
        binding.orderInfo.text =
            "Mijoz: ${data.client}\nMashina nomeri: ${data.carNum}\nTelefon raqami: ${data.phone}"
    }

    private fun initAction() {
        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sendHistory -> {
                        findNavController().navigate(R.id.feedSendHistoryFragment)
                        Toast.makeText(requireContext(), "Yuborish tarixi", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.brandBalance -> {
                        findNavController().navigate(R.id.brandBalanceFragment)
                        Toast.makeText(requireContext(), "Tovar qoldiq", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.option_menu_balance)
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
        binding.textView35.setOnClickListener {
            sendOrder()
        }
        binding.carImag2.setOnClickListener {
            pickCameraImage()
        }
    }

    private fun pickCameraImage() {
        cameraLauncher.launch(
            ImagePicker.with(requireActivity())
                .cameraOnly()
                .maxResultSize(1080, 1920, true)
                .createIntent()
        )
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
    }

    private fun sendOrder() {
        progressDialog.show()
//        val files=File("").compress2(requireContext())
        val files=File(filePath!!).compress2(requireContext())

        val builder: MultipartBody.Builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("id", orderId!!.toString())
        builder.addFormDataPart("img", files.name, files.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val body = builder.build()
        lifecycleScope.launchWhenStarted {
            viewModel.sendOrderFinal(body)
            viewModel.orderFinalState.collect {
                when(it){
                    is UIState.Success->{
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
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


}