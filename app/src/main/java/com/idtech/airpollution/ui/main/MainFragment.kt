package com.idtech.airpollution.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idtech.airpollution.databinding.MainFragmentBinding
import com.idtech.airpollution.ui.main.center.CenterAdapter
import com.idtech.airpollution.ui.main.header.HeaderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: MainFragmentBinding
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var headerAdapter:HeaderAdapter
    private lateinit var centerAdapter:CenterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            vm = this@MainFragment.viewModel
            lifecycleOwner = this@MainFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        viewModel.getApi()
    }

    fun initView(){
        headerAdapter = HeaderAdapter()
        centerAdapter = CenterAdapter()
        binding.rcvHeader.adapter = headerAdapter
        binding.rcvCenter.adapter = centerAdapter
    }
    fun initObserver(){
        viewModel.apply {
            setCenterData.observe(viewLifecycleOwner){
                centerAdapter.submitList(it)
            }
            setHeaderData.observe(viewLifecycleOwner){
                headerAdapter.submitList(it)
            }
        }
    }



}