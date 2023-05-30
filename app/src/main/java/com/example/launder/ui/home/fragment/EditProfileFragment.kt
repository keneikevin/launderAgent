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
import com.example.agent.R
import com.example.agent.databinding.FragmentEditprofileBinding
import com.example.launder.MainActivity
import com.example.launder.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {
    private lateinit var binding: FragmentEditprofileBinding

    lateinit var auth: FirebaseAuth
    private val viewModel:AuthViewModel by viewModels()


    private var cuImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditprofileBinding.bind(view)
        //   subscribeToObservers()
        auth = FirebaseAuth.getInstance()

        auth.currentUser?.let {
            binding.ivPostImage.setImageURI(it.photoUrl)
            binding.etCakeName.setText(it.displayName)
            binding.etPriceName.setText(it.email)
            binding.etPriceN.setText(it.phoneNumber)
        }
       // binding.ivPostImage.setImageURI(cuImageUri)
        binding.btnPost.setOnClickListener {
            updateProfile()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
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

    private fun updateProfile() {
        val user = auth.currentUser
        user?.let { user ->
            val username = binding.etCakeName.text.toString()
            val photoURI = cuImageUri
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoURI)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdates).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Successfully updated profile",
                            Toast.LENGTH_LONG).show()
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
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
            cuImageUri = data?.data
      //      data?.data?.let { viewModel.setCurImageUri(it) }
    }}
}





























