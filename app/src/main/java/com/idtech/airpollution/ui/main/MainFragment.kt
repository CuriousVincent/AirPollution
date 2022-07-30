package com.idtech.airpollution.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.idtech.airpollution.MainActivity
import com.idtech.airpollution.MainSharedViewModel
import com.idtech.airpollution.R
import com.idtech.airpollution.databinding.MainFragmentBinding
import com.idtech.airpollution.di.createMockWebService
import com.idtech.airpollution.ui.main.center.CenterAdapter
import com.idtech.airpollution.ui.main.header.HeaderAdapter
import com.orhanobut.logger.Logger
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
    private var act: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        act = requireActivity() as MainActivity
    }

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
        act?.setSupportActionBar(binding.toolbar)
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
                Logger.d("center item number ${centerAdapter.itemCount}")
            }
            setHeaderData.observe(viewLifecycleOwner){
                headerAdapter.submitList(it)
            }
            showNetworkErrorDialog.observe(viewLifecycleOwner){
                val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                builder.setTitle(R.string.error_title)
                    .setMessage(R.string.error_network)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.retry){_,_->
                        viewModel.getApi()
                    }
                    .show()
            }
            showErrorDialog.observe(viewLifecycleOwner){
                val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                builder.setTitle(R.string.error_title)
                    .setMessage(it)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.confirm){_,_-> }
                    .show()
            }
            setAllData.observe(viewLifecycleOwner){
                act?.airPollutionService?.apply {
                    allData = it
                }
            }
        }
        sharedViewModel.apply {
            searchWord.observe(viewLifecycleOwner) {
                act?.airPollutionService?.apply {
                    viewModel.searchWord(it, allData)
                }

            }
            isSearch.observe(viewLifecycleOwner){
                act?.airPollutionService?.apply {
                    viewModel.searchFlow(it, allData)
                }
            }
        }
    }



}