package com.example.launderagent.ui.home.customer

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agent.R
import com.example.agent.databinding.FragmentCartBinding
import com.example.launderagent.adapter.ShoppingAdapter
import com.example.launderagent.data.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment :Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    lateinit var viewModel: ShoppingViewModel
    lateinit var vm: MainViewModel
    private lateinit var shoppingAdapter: ShoppingAdapter
    //lateinit var daraja: Daraja
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        vm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCartBinding.bind(view)
        subscribeToObservers()
        setupRecyclerView()




        binding.fab.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cake City")
            val sizePice = "Proceed to checkout?    subtotal ${binding.total.text} "
            builder.setMessage(sizePice)
            builder.setIcon(R.drawable.a)
            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->

                    vm.bookServices()

               // findNavController().navigate(R.id.action_shoppingFragment_to_ordersFragment)
            }
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                /*NO-Op*/
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

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
            val pos = viewHolder.layoutPosition
            val item = shoppingAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }


    private fun subscribeToObservers() {
        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "$price ksh"
            binding.total.text = priceText
        })
        viewModel.shoppingItems.observe(viewLifecycleOwner, Observer {
            shoppingAdapter.shoppingItems = it
        })

    }
    private fun setupRecyclerView() {
        binding.rvCakes.apply {
            shoppingAdapter = ShoppingAdapter()
            adapter = shoppingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}
