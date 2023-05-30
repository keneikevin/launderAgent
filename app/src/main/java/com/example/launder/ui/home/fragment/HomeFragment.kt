package com.example.launder.ui.home.fragment


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.launder.adapter.CakeAdapter
import com.example.launder.ui.homepackage.CreateServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment :Fragment(R.layout.fragment_home){
    @Inject
    lateinit var cakeAdapter: CakeAdapter
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel : CreateServiceViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(requireActivity()).get(CreateServiceViewModel::class.java)
        binding = FragmentHomeBinding.bind(view)
        setUpRecyclerView()
        binding.fab.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment_to_createServiceFragment)
        }
    }

    private fun setUpRecyclerView()= binding.rvCakes.apply{
        cakeAdapter=CakeAdapter()
        binding.rvCakes.adapter = cakeAdapter
        binding.rvCakes.layoutManager = LinearLayoutManager(requireContext())
        itemAnimator = null

        lifecycleScope.launch {
            viewModel.flow.collect {
                cakeAdapter.submitData(it)
            }
        }


        lifecycleScope.launch {
            cakeAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading ||
                        it.append is LoadState.Loading
            }
        }
    }

}






























