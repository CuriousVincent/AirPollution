package com.idtech.airpollution.ui.main.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.idtech.airpollution.databinding.ItemHeaderListBinding
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.BindingViewHolder

class HeaderAdapter: ListAdapter<Record, BindingViewHolder<ItemHeaderListBinding>>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemHeaderListBinding> {
        val binding = ItemHeaderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemHeaderListBinding>, position: Int) {
        holder.binding.vm = ItemHeaderViewModel(getItem(position))
    }
}


class DiffCallback : DiffUtil.ItemCallback<Record>() {

    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.siteId == newItem.siteId && oldItem.siteName == newItem.siteName &&  oldItem.pm2_5 == newItem.pm2_5
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}