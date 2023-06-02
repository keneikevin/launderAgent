package com.example.launderagent.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentEditprofileBinding
import com.example.launderagent.other.Status
import com.example.launderagent.data.entities.ProfileUpdate
import com.example.launderagent.data.MainViewModel
import com.example.launderagent.other.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {
    private lateinit var binding: FragmentEditprofileBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var glide: RequestManager
    lateinit var auth: FirebaseAuth


    private var cuImageUri: Uri? = null
     lateinit var localions: String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditprofileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        subscribeToObservers()
        val uid = auth.uid!!
        viewModel.getUser(uid)

        val spinner = binding.spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.locations,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                 localions = parent?.getItemAtPosition(position) as String
               Toast.makeText(requireContext(), localions, Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        binding.btnPost.setOnClickListener {
            val username = binding.etCakeName.text.toString()
            val email = binding.etPriceName.text.toString()
            val phone = binding.etPriceN.text.toString()

            val profileUpdate = ProfileUpdate(auth.uid.toString(),username,email,phone,localions,cuImageUri)
            viewModel.updateProfile(profileUpdate)

        }

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
    }
    private fun subscribeToObservers() {


        viewModel.updateProfileStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{
                        binding.createPostProgressBar.visibility =  View.GONE
                        binding.btnPost.isClickable = true
                        snackbar("profile updated Successfully")
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                    }
                    Status.ERROR ->{
                                    binding.createPostProgressBar.visibility = View.GONE
                        binding.btnPost.isClickable = true
            snackbar(it.message.toString())
                    }
                    Status.LOADING ->{binding.createPostProgressBar.visibility = View.VISIBLE
                        binding.btnPost.isClickable = false
                    }
                }
            }

        })
        viewModel.getUserStatus.observe(viewLifecycleOwner, Observer {result->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{
                        binding.createPostProgressBar.visibility = View.GONE
                        glide.load(it.data?.profilePictureUrl).into(binding.ivPostImage)
                        binding.etCakeName.setText(it.data?.username)
                        binding.etPriceN.setText(it.data?.phone)
                        binding.etPriceName.setText(it.data?.email)
                    }
                    Status.ERROR ->{
                        binding.createPostProgressBar.visibility = View.GONE
                        snackbar(it.message.toString())
                    }
                    Status.LOADING ->{ binding.createPostProgressBar.visibility = View.VISIBLE}
                }
            }

        })
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
            cuImageUri = data?.data
      //      data?.data?.let { viewModel.setCurImageUri(it) }
    }}
}





























