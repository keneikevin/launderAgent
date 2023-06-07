package com.example.launderagent.ui.home.agent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentServiceBinding
import com.example.launderagent.adapterpackage.ServiceAdapter
import com.example.launderagent.other.Status
import com.example.launderagent.data.MainViewModel
import com.example.launderagent.other.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ServiceFragment : Fragment(R.layout.fragment_service) {

    @Inject
    lateinit var glide:RequestManager
    @Inject
    lateinit var serviveAdapter: ServiceAdapter
    private lateinit var binding: FragmentServiceBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentServiceBinding.bind(view)
        subscribeToObservers()
        setUpRecylerView()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_createServiceFragment)
        }
        serviveAdapter.notifyDataSetChanged()


        requireActivity().title = "My Services"
        serviveAdapter.notifyDataSetChanged()

    }

    override fun onPause() {
        super.onPause()
        // Restore the previous title when the fragment is destroyed
        requireActivity().title = "Launder Agent"
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
            val pos = viewHolder.layoutPosition
            val item = serviveAdapter.posts[pos]

            viewModel.deleteService(item)
            serviveAdapter.posts -=item

            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).show()
        }
    }
    private fun setUpRecylerView() = binding.rvCakes.apply{
        viewModel.getService()
        serviveAdapter= ServiceAdapter(glide)
        binding.rvCakes.adapter = serviveAdapter
        binding.rvCakes.layoutManager = LinearLayoutManager(requireContext())

        adapter=serviveAdapter
        layoutManager = LinearLayoutManager(requireContext())
        itemAnimator = null

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        serviveAdapter.notifyDataSetChanged()
    }


        private fun subscribeToObservers(){

            viewModel.deleteServiceStatus.observe(viewLifecycleOwner, Observer { result ->
                result?.let {
                    when (result.status) {
                        Status.SUCCESS ->{}
                        Status.LOADING ->{}
                        Status.ERROR ->{  snackbar(it.message.toString())}

                    }
                }
            })

            viewModel.services.observe(viewLifecycleOwner, Observer { result ->
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
















