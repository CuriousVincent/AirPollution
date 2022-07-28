package com.idtech.airpollution.ui.main.center

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.idtech.airpollution.databinding.ItemCenterListBinding
import com.idtech.airpollution.databinding.ItemHeaderListBinding
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.ui.main.header.DiffCallback
import com.idtech.airpollution.ui.main.header.ItemHeaderViewModel
import com.idtech.airpollution.utils.BindingViewHolder

class CenterAdapter: ListAdapter<Record, BindingViewHolder<ItemCenterListBinding>>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemCenterListBinding> {
        val binding = ItemCenterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemCenterListBinding>, position: Int) {
        holder.binding.vm = ItemCenterViewModel(getItem(position))
    }

}