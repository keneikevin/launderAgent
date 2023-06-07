package com.example.launderagent.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentOrderBinding
import com.example.launderagent.other.Status
import com.example.launderagent.other.snackbar
import com.example.launderagent.adapterpackage.OrdersAdapter
import com.example.launderagent.data.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_order) {

    @Inject
    lateinit var glide:RequestManager
    @Inject
    lateinit var serviveAdapter: OrdersAdapter
    private lateinit var binding: FragmentOrderBinding
    private val viewModel: MainViewModel by viewModels()





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderBinding.bind(view)
        subscribeToObservers()
        setUpRecylerView()
        requireActivity().title = "My Orders"
        serviveAdapter.notifyDataSetChanged()

    }

    override fun onPause() {
        super.onPause()
        // Restore the previous title when the fragment is destroyed
        requireActivity().title = "Launder Agent"
    }


    private fun setUpRecylerView() = binding.rvCakes.apply{
        viewModel.getOrders()
        serviveAdapter= OrdersAdapter(glide)
        binding.rvCakes.adapter = serviveAdapter
        binding.rvCakes.layoutManager = LinearLayoutManager(requireContext())

        adapter=serviveAdapter
        layoutManager = LinearLayoutManager(requireContext())
        itemAnimator = null

        serviveAdapter.notifyDataSetChanged()
    }


    private fun subscribeToObservers(){
        viewModel.deleteOrderStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{}
                    Status.LOADING ->{}
                    Status.ERROR ->{  snackbar(it.message.toString())}

                }
            }
        })
        viewModel.orders.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{
                        serviveAdapter.posts = it.data!!
                        binding.progressBar.visibility =  View.GONE
                    }
                    Status.ERROR ->{
                        binding.progressBar.visibility = View.GONE
                        snackbar(it.message.toString())
                    }
                    Status.LOADING ->{binding.progressBar.visibility = View.VISIBLE}
                }
            }

        })

    }
}


























