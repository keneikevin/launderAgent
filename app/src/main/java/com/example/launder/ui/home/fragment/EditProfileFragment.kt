package com.example.launder.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.agent.databinding.FragmentEditprofileBinding
import com.example.launder.MainActivity
import com.example.launder.data.Status
import com.example.launder.data.entities.ProfileUpdate
import com.example.launder.data.other.EventObserver
import com.example.launder.ui.auth.AuthViewModel
import com.example.launder.ui.home.snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.HashMap
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {
    private lateinit var binding: FragmentEditprofileBinding
    @Inject
    lateinit var glide: RequestManager
    lateinit var auth: FirebaseAuth
    private val viewModel:AuthViewModel by viewModels()


    private var cuImageUri: Uri? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditprofileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        subscribeToObservers()
        val uid = auth.uid!!
        viewModel.getUser(uid)

        binding.btnPost.setOnClickListener {
            val username = binding.etCakeName.text.toString()
            val email = binding.etPriceName.text.toString()
            val phone = binding.etPriceN.text.toString()

            val profileUpdate = ProfileUpdate(auth.uid.toString(),username,email,phone,"",cuImageUri)
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
                        snackbar("profile updated Successfully")
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
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
        viewModel.getUserStatus.observe(viewLifecycleOwner, Observer {

            binding.createPostProgressBar.visibility = View.GONE
            glide.load(it.peekContent().data?.profilePictureUrl).into(binding.ivPostImage)
            binding.etCakeName.setText(it.peekContent().data?.username)
            binding.etPriceN.setText(it.peekContent().data?.phone)
            binding.etPriceName.setText(it.peekContent().data?.email)
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





























