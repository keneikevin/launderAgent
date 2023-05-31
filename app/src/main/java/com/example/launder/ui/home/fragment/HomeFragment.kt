package com.example.launder.ui.home.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.launder.adapter.CakeAdapter
import com.example.launder.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.example.launder.ui.home.snackbar
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class HomeFragment :Fragment(R.layout.fragment_home){
    @Inject
    lateinit var cakeAdapter: CakeAdapter
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel : AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        setUpRecyclerView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentHomeBinding.bind(view)
        setUpRecyclerView()
        binding.fab.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment_to_createServiceFragment)
        }
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            cakeAdapter=CakeAdapter()
//            val pos = viewHolder.layoutPosition
//           // val item = cakeAdapter.shoppingItems[pos]
//            val item = cakeAdapter.shoppingItems[pos]
//            viewModel?.deletePost(item)
//            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
//                setAction("Undo") {
//                    viewModel?.createPost(item)
//                }
//                show()
//            }
        }
    }
    private fun setUpRecyclerView()= binding.rvCakes.apply{
        snackbar("swipe to delete")
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






























