package com.example.launderagent.ui.home.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.agent.R
import com.example.agent.databinding.FragmentDetailBinding
import com.example.launderagent.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: ShoppingViewModel
    private val args:DetailFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        binding = FragmentDetailBinding.bind(view)
        if (args.currentService.mediaId.isNotEmpty()){
            viewModel.getServiceById(args.currentService.mediaId)
            subscribeToObservers()
        }

        Glide.with(this.requireContext())
            .load(args.currentService.img).into(binding.bigImage)

        args.currentService.price.let {
            viewModel.setCurPrice(it)
        }
        viewModel.items()

        binding.etShoppingItemName.text = args.currentService.title
        binding.per.text = "${args.currentService.per}s"
        binding.picker.minValue = 1
        binding.picker.maxValue = 10
        binding.picker.setOnValueChangedListener{ picker, oldVal, newVal ->
            val valuePicker1: Int = binding.picker.value
            val text = "Changed from $oldVal to $newVal"

            viewModel.calculate(binding.picker.value.toString())

        }

        binding.btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.picker.value.toString(),
                binding.imageView7.text.toString(),
                args.currentService.img.toString()
            )

        }
    }


    private fun subscribeToObservers(){
        viewModel.cake.observe(viewLifecycleOwner, Observer {
            it
        })
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->

            binding.imageView7.text = newScore.toString()

        })

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.let { result ->
                when (result.status) {

                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    Status.SUCCESS -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "Added to Cart",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                  }
                    Status.LOADING -> {
                        /*NO OP*/
                    }
                }
            }
        }
        )

    }

    override fun onPause() {
        super.onPause()
        viewModel.setCurPrice("")
    }

}


























