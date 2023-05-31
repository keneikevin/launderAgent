package com.example.launder.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentCreateBinding
import com.example.launder.data.Status
import com.example.launder.ui.auth.AuthViewModel
import com.example.launder.ui.home.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateServiceFragment : Fragment(R.layout.fragment_create) {
    private lateinit var binding: FragmentCreateBinding


    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    private var curImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateBinding.bind(view)
           subscribeToObservers()
        binding.ivPostImage.setImageURI(curImageUri)
        binding.btnSetPostImage.setOnClickListener {
            //check runtime permission
            if (checkSelfPermission(this.requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PermissionChecker.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery()
            }
        }
        val stringArray = arrayOf("Item","Kg","Pair")
        binding.picker.displayedValues = stringArray
       val numberPicker = binding.picker
        numberPicker.value = 2
        numberPicker.minValue = 0
        numberPicker.maxValue = stringArray.size - 1
        numberPicker.displayedValues = stringArray
        numberPicker.wrapSelectorWheel = true
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            val selectedValue = stringArray[newVal]
            Toast.makeText(requireContext(), stringArray[newVal], Toast.LENGTH_SHORT).show()

        }
        binding.btnPost.setOnClickListener {

            curImageUri?.let { uri ->
                viewModel.createPost(uri, binding.etCakeName.text.toString(),binding.etPriceName.text.toString(),stringArray[numberPicker.value])
            } ?: snackbar(getString(R.string.error_no_image_chosen))
          //  findNavController().navigate(R.id.action_createServiceFragment_to_homeFragment)
        }
    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this.requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.ivPostImage.setImageURI(data?.data)
            curImageUri = data?.data
            data?.data?.let { viewModel.setCurImageUri(it) }

        }
    }
    private fun subscribeToObservers() {


        viewModel.createPostStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{
                        binding.createPostProgressBar.visibility =  View.GONE
                        snackbar("Service created Successfully")
                        findNavController().navigate(R.id.action_createServiceFragment_to_homeFragment)
                    }
                    Status.ERROR ->{
                        binding.createPostProgressBar.visibility = View.GONE
                        binding.createPostProgressBar.visibility = View.GONE
                        snackbar(it.message.toString())
                    }
                    Status.LOADING ->{binding.createPostProgressBar.visibility = View.VISIBLE}
                }
            }

        })

    }
}





























