package com.example.launderagent.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
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
import java.util.Date
import java.util.Locale
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
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val readableDate = dateFormat.format(currentDate)
            val profileUpdate = OrderUpdate(status = "Status", orderId = args.currentOrder.orderId, completeTime = readableDate)

                viewModel.updateOrder(profileUpdate)
        }

        binding.btnSetPostImage.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Launder")
            val sizePice = "Proceed to delete oder: ${args.currentOrder.code} ?"
            builder.setMessage(sizePice)
            builder.setIcon(R.drawable.a)
            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->


                viewModel.deleteOrder(args.currentOrder)

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
    private fun subscribeToObservers() {
        viewModel.deleteOrderStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{
                        findNavController().popBackStack()
                    }
                    Status.LOADING ->{}
                    Status.ERROR ->{  snackbar(it.message.toString())}

                }
            }
        })
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


}





























