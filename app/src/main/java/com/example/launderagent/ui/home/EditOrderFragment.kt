package com.example.launderagent.ui.home

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentEditorderBinding
import com.example.launderagent.other.Status
import com.example.launderagent.data.MainViewModel
import com.example.launderagent.data.entities.OrderUpdate
import com.example.launderagent.other.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class EditOrderFragment : Fragment(R.layout.fragment_editorder) {
    private lateinit var binding: FragmentEditorderBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var glide: RequestManager
    lateinit var auth: FirebaseAuth

    private val args: EditOrderFragmentArgs by navArgs()



    private var cuImageUri: Uri? = null
     lateinit var localions: String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.ivPostImage.setImageURI(data?.data)
            data?.data?.let { viewModel.setCurImageUri(it) }
        }}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditorderBinding.bind(view)
        if (args.currentOrder.oderUid.isNotEmpty()){
         //   viewModel.getOrders(args.currentOrder.orderUid)
            viewModel.getUser(args.currentOrder.oderUid)
            binding.loc
            subscribeToObservers()
        }

        auth = FirebaseAuth.getInstance()
        val uid = auth.uid!!
        viewModel.getUser(uid)
        binding.btnPost.text = args.currentOrder.status
        binding.loc.text = args.currentOrder.oderUid


        binding.btnPost.setOnClickListener {
//            val username = binding.etCakeName.text.toString()
//            val email = binding.etPriceName.text.toString()
//            val phone = binding.etPriceN.text.toString()

            val profileUpdate = OrderUpdate(status = "Status", orderId = args.currentOrder.orderId)

                viewModel.updateOrder(profileUpdate)
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

        viewModel.updateOrderStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{

                        binding.createPostProgressBar.visibility =  View.GONE
                        binding.btnPost.isClickable = true
                        snackbar("Order updated Successfully")
                        findNavController().popBackStack()

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
        viewModel.curImageUri.observe(viewLifecycleOwner){uri->
          uri?.let {
              cuImageUri=it
              Log.d("raetat",it.toString())
          }
        }
        viewModel.getUserStatus.observe(viewLifecycleOwner, Observer {result->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{

                        binding.createPostProgressBar.visibility = View.GONE
                        glide.load(it.data?.profilePictureUrl).into(binding.ivPostImage)
//                        binding.etCakeName.setText(it.data?.username)
//                        binding.etPriceN.setText(it.data?.phone)
//                        binding.etPriceName.setText(it.data?.email)
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

}





























