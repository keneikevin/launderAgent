package com.example.launderagent.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentCustomersServiceBinding
import com.example.launderagent.adapterpackage.ServiceAdapter
import com.example.launderagent.adapterpackage.ServiceCustomerAdapter
import com.example.launderagent.data.MainViewModel
import com.example.launderagent.data.entities.Service
import com.example.launderagent.other.Status
import com.example.launderagent.other.snackbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CustomersServiceFragment : Fragment(R.layout.fragment_customers_service) {

    @Inject
    lateinit var glide:RequestManager
    @Inject
    lateinit var serviveAdapter: ServiceCustomerAdapter
    private lateinit var binding: FragmentCustomersServiceBinding
    private lateinit var sss: List<Service>
    private val viewModel: MainViewModel by viewModels()
    protected open val uid:String
        get() = FirebaseAuth.getInstance().uid!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCustomersServiceBinding.bind(view)
        subscribeToObservers()
        setUpRecylerView()
        viewModel.loadOrder(uid)

        binding.fab.setOnClickListener {
          viewModel.bookServices(
              code = "String",
              status = "Pending",
              bookTime = "8882",
              completeTime ="8882",
              prise = binding.total.text.toString()
          )
        }
        serviveAdapter.notifyDataSetChanged()

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
        serviveAdapter= ServiceCustomerAdapter(glide)
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
            viewModel.bookServiceStatus.observe(viewLifecycleOwner, Observer { result ->
                result?.let {
                    when (result.status) {
                        Status.SUCCESS ->{
//                            binding.createPostProgressBar.visibility =  View.GONE
//                            binding.btnPost.isClickable = true
                            snackbar("Service created Successfully")

                  //          findNavController().navigate(R.id.action_createServiceFragment_to_serviceFragment)
                        }
                        Status.ERROR ->{
//                            binding.createPostProgressBar.visibility = View.GONE
//                            binding.btnPost.isClickable = true
                            snackbar(it.message.toString())
                        }
                        Status.LOADING ->{
//                            binding.createPostProgressBar.visibility = View.VISIBLE
//                            binding.btnPost.isClickable = false
                        }
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
















