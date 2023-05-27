package com.example.launder.ui.home.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.agent.R
import com.example.agent.databinding.FragmentCreateServiceBinding
import com.example.launder.data.Status
import com.example.launder.ui.auth.AuthViewModel
import com.example.launder.ui.home.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateServiceFragment : Fragment(R.layout.fragment_create_service) {
    private lateinit var binding: FragmentCreateServiceBinding


    private val viewModel:AuthViewModel by viewModels()

    private lateinit var cropContent: ActivityResultLauncher<Any?>


    private var curImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        cropContent = registerForActivityResult(cropActivityResultContract) {
//            it?.let {
//                viewModel.setCurImageUri(it)
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateServiceBinding.bind(view)
     //   subscribeToObservers()
        binding.btnSetPostImage.setOnClickListener {
            cropContent.launch(null)
        }
        binding.ivPostImage.setOnClickListener {
            cropContent.launch(null)
        }
//        binding.btnPost.setOnClickListener {
//            curImageUri?.let { uri ->
//                viewModel.createPost(uri, binding.etCakeName.text.toString(),binding.etPriceName.text.toString())
//            } ?: snackbar(getString(R.string.error_no_image_chosen))
//        }
    }
//
//    private fun subscribeToObservers() {
//        viewModel.curImageUri.observe(viewLifecycleOwner) {
//            curImageUri = it
//            binding.btnSetPostImage.isVisible = false
//            Glide.with(this).load(curImageUri).into(binding.ivPostImage)
//        }
//        viewModel.createPostStatus.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let { result ->
//                when (result.status) {
//                    Status.ERROR -> {
//                        binding.createPostProgressBar.isVisible = false
//                        snackbar(it.toString())
//                    }
//                    Status.SUCCESS -> {
//                        binding.createPostProgressBar.isVisible = false
//                        Toast.makeText(requireActivity(),"added", Toast.LENGTH_LONG).show()
//                        findNavController().popBackStack()
//
//                    }
//                    Status.LOADING -> {
//                        binding.createPostProgressBar.isVisible = true
//                    }
//                }
//            }
//        })
//    }
}





























