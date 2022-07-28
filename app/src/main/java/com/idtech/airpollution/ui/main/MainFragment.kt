package com.idtech.airpollution.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.idtech.airpollution.MainSharedViewModel
import com.idtech.airpollution.databinding.MainFragmentBinding
import com.idtech.airpollution.ui.main.center.CenterAdapter
import com.idtech.airpollution.ui.main.header.HeaderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: MainFragmentBinding
    private val viewModel by viewModel<MainViewModel>()
    private val sharedViewModel by sharedViewModel<MainSharedViewModel>()
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

   private fun initView(){
        headerAdapter = HeaderAdapter()
        centerAdapter = CenterAdapter{
            val text = "${it.siteName} ${it.pm2_5}"
            Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show()
        }
        binding.rcvHeader.adapter = headerAdapter
        binding.rcvCenter.adapter = centerAdapter
    }
    private fun initObserver(){
        viewModel.apply {
            setCenterData.observe(viewLifecycleOwner){
                centerAdapter.submitList(it)
            }
            setHeaderData.observe(viewLifecycleOwner){
                headerAdapter.submitList(it)
            }
        }
        sharedViewModel.apply {
            searchWord.observe(viewLifecycleOwner) {
                viewModel.searchWord(it)
            }
            isSearch.observe(viewLifecycleOwner){
                viewModel.searchFlow(it)
            }
        }
    }



}