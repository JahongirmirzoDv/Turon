package com.example.turon.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.turon.R
import com.example.turon.data.model.AcceptProductScanner
import com.example.turon.data.model.SharedViewModel
import com.example.turon.databinding.ItemScannerLayoutBinding


class AcceptScanAdapter(
    var list: ArrayList<AcceptProductScanner>,
    private val activity: FragmentActivity
) :
    RecyclerView.Adapter<AcceptScanAdapter.VH>() {
    var isEnable = false
    var isSelectAll = false
    var selectList: ArrayList<AcceptProductScanner> = arrayListOf()
    val viewModel = ViewModelProvider(activity).get(SharedViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemBinding =
            ItemScannerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return VH(itemBinding)
    }

    inner class VH(private val binding: ItemScannerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(data: AcceptProductScanner, activity: FragmentActivity) {
            binding.apply {
                text1.text = (adapterPosition + 1).toString()
                text2.text = data.productName
                text3.text = data.crCode.toString()

                root.setOnLongClickListener {
                    if (!isEnable) {
                        val callback = object : ActionMode.Callback {
                            override fun onActionItemClicked(
                                mode: ActionMode?,
                                item: MenuItem?
                            ): Boolean {
                                when (item!!.itemId) {
                                    R.id.menu_delate -> {
                                        selectList.forEach {
                                            list.remove(it)
                                        }
                                        mode?.finish()
                                    }
                                    R.id.menu_select_all -> {
                                        if (selectList.size == list.size) {
                                            isSelectAll = true
                                            selectList.clear()

                                        } else {
                                            isSelectAll = false
                                            selectList.clear()
                                            selectList.addAll(list)

                                        }
                                        viewModel.setText(selectList.size.toString())
                                        notifyDataSetChanged()
                                        mode?.finish()
                                    }
                                }

                                return true
                            }

                            override fun onCreateActionMode(
                                mode: ActionMode?,
                                menu: Menu?
                            ): Boolean {
                                val inflater = mode?.menuInflater
                                inflater?.inflate(R.menu.action_mode_menu, menu)
                                return true
                            }

                            override fun onPrepareActionMode(
                                mode: ActionMode?,
                                menu: Menu?
                            ): Boolean {
                                menu?.findItem(R.id.menu_delate)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                                isEnable = true
                                val s = list[adapterPosition]
                                if (!childCheckbox.isVisible) {
                                    childCheckbox.isVisible = true
                                    root.alpha = 1f
                                    selectList.add(s)
                                } else {
                                    childCheckbox.isVisible = false
                                    root.alpha = 0.5f
                                    selectList.remove(s)
                                }
                                viewModel.setText(selectList.size.toString())

                                viewModel.getText().observe(activity, {
                                    mode?.title = String.format("%s Selected", s)
                                })

                                return true
                            }

                            override fun onDestroyActionMode(mode: ActionMode?) {

                                isEnable=false
                                isSelectAll=false
                                selectList.clear()
                                notifyDataSetChanged()

                            }
                        }
                        activity.startActionMode(callback)

                    }else{
                        val s = list[adapterPosition]
                        if (!childCheckbox.isVisible) {
                            childCheckbox.isVisible = true
                            root.alpha = 0.5f
                            selectList.add(s)
                        } else {
                            childCheckbox.isVisible = false
                            root.alpha = 1f
                            selectList.remove(s)
                        }
                        viewModel.setText(selectList.size.toString())
                    }

                    return@setOnLongClickListener true
                }

                root.setOnClickListener {
                    if (isEnable){
                        val s = list[adapterPosition]
                        if (!childCheckbox.isVisible) {
                            childCheckbox.isVisible = true
                            root.alpha = 1f
                            selectList.add(s)
                        } else {
                            childCheckbox.isVisible = false
                            root.alpha = 0.5f
                            selectList.remove(s)
                        }
                        viewModel.setText(selectList.size.toString())
//                        clickItem
                    }
                    else{

                    }
                }

                if (isSelectAll){
                    childCheckbox.isVisible=true
                    root.alpha=0.5f
                }
                else{
                    childCheckbox.isVisible=false
                    root.alpha=1f
                }
            }

        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position], activity)
    }

    override fun getItemCount() = list.size

}