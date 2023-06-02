package com.example.launderagent.ui.home.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.agent.databinding.FragmentHomeBinding
import com.example.launderagent.adapterpackage.ServiceAdapter
import com.example.launderagent.data.Status
import com.example.launderagent.data.other.DeletePostDialog
import com.example.launderagent.ui.auth.AuthViewModel
import com.example.launderagent.ui.auth.HomeViewModel
import com.example.launderagent.ui.home.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BasePostFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var glide:RequestManager
    @Inject
    lateinit var cakeAdapter: ServiceAdapter
    private lateinit var binding: FragmentHomeBinding
    private val basePostViewModel: HomeViewModel by viewModels()
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        subscribeToObservers()
        setUpRecylerView()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_basePostFragment_to_createServiceFragment)
        }
        cakeAdapter.notifyDataSetChanged()



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
            val item = cakeAdapter.posts[pos]

            basePostViewModel.deletePost(item)
            cakeAdapter.posts -=item

            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).show()
        }
    }
    private fun setUpRecylerView() = binding.rvCakes.apply{
        cakeAdapter= ServiceAdapter(glide)
        binding.rvCakes.adapter = cakeAdapter
        binding.rvCakes.layoutManager = LinearLayoutManager(requireContext())

        adapter=cakeAdapter
        layoutManager = LinearLayoutManager(requireContext())
        itemAnimator = null

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        cakeAdapter.notifyDataSetChanged()
    }


        private fun subscribeToObservers(){

            basePostViewModel.deletePostStatus.observe(viewLifecycleOwner, Observer {  result ->
                result?.let {
                    when (result.status) {
                        Status.SUCCESS ->{}
                        Status.LOADING ->{}
                        Status.ERROR ->{  snackbar(it.message.toString())}

                    }
                }
            })
            basePostViewModel.post.observe(viewLifecycleOwner, Observer {
                cakeAdapter.posts = it

            })

    }
}
















