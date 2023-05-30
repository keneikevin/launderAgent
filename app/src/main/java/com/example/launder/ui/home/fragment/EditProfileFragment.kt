package com.example.launder.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.agent.R
import com.example.agent.databinding.FragmentCreateBinding
import com.example.agent.databinding.FragmentEditprofileBinding
import com.example.launder.MainActivity
import com.example.launder.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {
    private lateinit var binding: FragmentEditprofileBinding


    private val viewModel:AuthViewModel by viewModels()


    private var curImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditprofileBinding.bind(view)
        //   subscribeToObservers()


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
        }
    }
}





























