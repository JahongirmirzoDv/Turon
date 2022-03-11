package com.example.turon.security.ui

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.R
import com.example.turon.adapter.TurnHistoryAdapter
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.factory.TurnAcceptViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.Activetashkent
import com.example.turon.databinding.FragmentActiveTurnBinding
import com.example.turon.security.viewmodels.TurnAcceptViewModel
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.flow.collect


class ActiveTurnFragment : Fragment(){
    private var _binding: FragmentActiveTurnBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: AlertDialog
    private lateinit var orderAdapter: TurnHistoryAdapter
    private lateinit var orderListVi: ArrayList<Activetashkent>
    private lateinit var orderListTo: ArrayList<Activetashkent>
    private var id: Int?= null
    private var state:Boolean=true
    private var position: Int? = null
    private val viewModel: TurnAcceptViewModel by viewModels {
        TurnAcceptViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveTurnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderListVi = ArrayList()
        orderListTo = ArrayList()
        setupUI()
        initAction()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
        getActiveTurn(true)
        itemSwipeMethod()
    }

    private fun itemSwipeMethod() {
        val callback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Take action for the swiped item
                try {
                    position = viewHolder.absoluteAdapterPosition
                    if (state){
                        id =  orderListTo[position!!].id
                        val status=orderListTo[position!!].status
                        deleteTurn(id!!,status)

                    }else{
                        id =  orderListVi[position!!].id
                        val status=orderListVi[position!!].status
                        deleteTurn(id!!,status)
                    }

                } catch (e: java.lang.Exception) {
                    Log.e("MainActivity", e.message!!)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.swipe_recycler_color
                        )
                    )
                    .addActionIcon(R.drawable.ic_remove)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive

                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerTurn)
    }


    private fun deleteTurn(id: Int,status:Int) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.carLeave(id,status)
            viewModel.carLeaveState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
//                        if (state) getActiveTurn(true)
//                        else getActiveTurn(false)
                        getActiveTurn(state)
                        orderAdapter.deleteItem(position!!)
                        toast(it.data.message)
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

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun getActiveTurn(st:Boolean) {
        progressDialog.show()
        lifecycleScope.launchWhenStarted {
            viewModel.getActiveTurn()
            viewModel.turnActiveState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        orderListVi.clear()
                        orderListTo.clear()
                        if (it.data.data.loadingviloyat.isEmpty() && st) {
                            toast("Viloyatdan navbatga olinganlar tugadi!.")
                            SharedPref(requireContext()).setTurnNumViloyat(1)
                        } else if (it.data.data.loadingtashkent.isEmpty()) {
                            toast("Toshkentdan navbatga olinganlar tugadi!.")
                            SharedPref(requireContext()).setTurnNumViloyat(1)
                        }
                        orderListVi.addAll(it.data.data.loadingviloyat)
                        orderListTo.addAll(it.data.data.loadingtashkent)

                        if (st){
                            orderAdapter = TurnHistoryAdapter(orderListTo)
                            binding.recyclerTurn.adapter = orderAdapter
                        }else{
                            orderAdapter = TurnHistoryAdapter(orderListVi)
                            binding.recyclerTurn.adapter = orderAdapter
                        }
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
        binding.btnShahar.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#FFCC66"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#E4D2B6"))
            state=true
            getActiveTurn(true)
        }
        binding.btnViloyat.setOnClickListener {
            binding.btnShahar.setBackgroundColor(Color.parseColor("#E4D2B6"))
            binding.btnViloyat.setBackgroundColor(Color.parseColor("#FFCC66"))
            state=false
            getActiveTurn(false)
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var queryListVi: ArrayList<Activetashkent> = ArrayList()
                var queryListTo: ArrayList<Activetashkent> =ArrayList()
                if (state){
                    if (charSequence == "") {
                        orderAdapter = TurnHistoryAdapter(orderListVi)
                        binding.recyclerTurn.adapter = orderAdapter
                    }
                    else {
                        queryListVi = ArrayList()
                        for (model in orderListVi) {
                            if (model.toString().lowercase()
                                    .contains(charSequence.toString().lowercase())
                            ) {
                                queryListVi.add(model)
                            }
                        }
                        orderAdapter = TurnHistoryAdapter(queryListVi)
                        binding.recyclerTurn.adapter = orderAdapter
                    }
                }
                else{
                    if (charSequence == "") {
                        orderAdapter = TurnHistoryAdapter(orderListTo)
                        binding.recyclerTurn.adapter = orderAdapter
                    }
                    else {
                        queryListTo = ArrayList()
                        for (model in orderListTo) {
                            if (model.toString().lowercase()
                                    .contains(charSequence.toString().lowercase())
                            ) {
                                queryListTo.add(model)
                            }
                        }
                        orderAdapter = TurnHistoryAdapter(queryListTo)
                        binding.recyclerTurn.adapter = orderAdapter
                    }
                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}
