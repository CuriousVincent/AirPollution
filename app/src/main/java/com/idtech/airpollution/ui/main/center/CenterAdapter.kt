package com.idtech.airpollution.ui.main.center

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idtech.airpollution.databinding.ItemCenterListBinding
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.ui.main.header.DiffCallback
import com.idtech.airpollution.utils.BindingViewHolder
import com.idtech.airpollution.utils.ContextUtils

class CenterAdapter(val callback: (record: Record) -> Unit) : ListAdapter<Record, BindingViewHolder<ItemCenterListBinding>>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemCenterListBinding> {
        val binding = ItemCenterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemCenterListBinding>, position: Int) {
        val viewModel = ItemCenterViewModel(getItem(position), ContextUtils(holder.binding.root.context))
        holder.binding.vm = viewModel
        holder.binding.root.setOnClickListener {
            if (viewModel.showArrow) {
                callback.invoke(viewModel.data)
            }
        }
    }
}

