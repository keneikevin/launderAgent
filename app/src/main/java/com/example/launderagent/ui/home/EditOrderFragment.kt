package com.example.launderagent.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentOrdereditBinding
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
class EditOrderFragment : Fragment(R.layout.fragment_orderedit) {
    private lateinit var binding: FragmentOrdereditBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var glide: RequestManager
    lateinit var auth: FirebaseAuth

    private val args: EditOrderFragmentArgs by navArgs()



    private var cuImageUri: Uri? = null
     lateinit var stats: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrdereditBinding.bind(view)
        if (args.currentOrder.customerOrderid.isNotEmpty()){
         //   viewModel.getOrders(args.currentOrder.orderUid)
            viewModel.getUser(args.currentOrder.customerOrderid)
         //   binding.loc
            subscribeToObservers()
        }

        auth = FirebaseAuth.getInstance()
        val uid = auth.uid!!
        viewModel.getUser(uid)
       // binding.btnPost.text = args.currentOrder.status
      //  binding.etShoppingItemName.text = args.currentOrder.code
        binding.orderid.text = args.currentOrder.code
        binding.book.text = args.currentOrder.bookTime
        binding.complete.text = args.currentOrder.completeTime
        binding.pricee.text = args.currentOrder.price
        binding.status.text = args.currentOrder.status
        stats = args.currentOrder.status
        when (args.currentOrder.status) {
            "Pending" -> {
                binding.status.setBackgroundColor(Color.parseColor("#0000FF"))
                stats = "Accepted"
                binding.button.text = "Accept Order"

            }

            "Accepted" -> {
                binding.status.setBackgroundColor(Color.parseColor("#800000"))
                stats = "Processing"
                binding.button.text = "Process Order"
            }
            "Processing" -> {
                binding.status.setBackgroundColor(Color.parseColor("#808080"))
                stats = "Complete"
                binding.button.text = "Complete Order"
            }
            "Complete" -> {
                binding.status.setBackgroundColor(Color.parseColor("#006400"))
                binding.button.setBackgroundColor(Color.parseColor("#006400"))
                binding.dele.visibility = View.VISIBLE
                binding.button.text = "Order Complete"
                binding.button.isClickable = false
                stats = "Complete"}
            "Canceled" -> {
                binding.dele.visibility = View.VISIBLE
                stats = "Canceled"
                binding.button.text = "Order Canceled"
                binding.button.setBackgroundColor(Color.RED)
                binding.button.isClickable = false
            }
            else ->  {
                binding.status.setBackgroundColor(Color.RED)
            }
        }
        if (args.currentOrder.status.equals("Processing") ||args.currentOrder.status.equals("Pending") || args.currentOrder.status.equals("Accepted")) {

            binding.button.setOnClickListener {
            stats
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val readableDate = dateFormat.format(currentDate)
            val profileUpdate = OrderUpdate(status = stats, orderId = args.currentOrder.orderId, completeTime = readableDate)

                viewModel.updateOrder(profileUpdate)
                findNavController().popBackStack();
        }}

        binding.dele.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Launder")
            val sizePice = "Proceed to delete oder: ${args.currentOrder.code} ?"
            builder.setMessage(sizePice)
            builder.setIcon(R.drawable.a)
            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->


                viewModel.deleteOrder(args.currentOrder)
                findNavController().popBackStack();
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

                        binding.progressBar.visibility =  View.GONE
                        binding.button.isClickable = true
                        snackbar("Order updated Successfully")
                        findNavController().popBackStack()

                    }
                    Status.ERROR ->{
                                    binding.progressBar.visibility = View.GONE
                        binding.button.isClickable = true
            snackbar(it.message.toString())
                    }
                    Status.LOADING ->{binding.progressBar.visibility = View.VISIBLE
                        binding.button.isClickable = false
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
        // Inside the onViewCreated() function after initializing your views and variables

        // Inside the onViewCreated() function after initializing your views and variables

        binding.ll.setOnClickListener {
            // Latitude and Longitude for Point A
            val latA = -1.3874  // Ongata Rongai latitude
            val lonA = 36.9673  // Ongata Rongai longitude

            // Latitude and Longitude for Point B
            val latB = -1.2921
            val lonB = 36.8219

            // Create a URI for Google Maps with polyline information
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=$latA,$lonA&daddr=$latB,$lonB&dirflg=d")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            // Verify that the intent will resolve to an activity
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // If Google Maps app is not installed, handle the situation as needed
                Toast.makeText(requireContext(), "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.getUserStatus.observe(viewLifecycleOwner, Observer {result->
            result?.let {
                when (result.status) {
                    Status.SUCCESS ->{

                        binding.progressBar.visibility = View.GONE
                        glide.load(it.data?.profilePictureUrl).into(binding.bigMage)
                        binding.etShoppingItemName.text = it.data?.username
                        binding.eEmail.text = it.data?.email
                        binding.ePhone.text = it.data?.phone
                        binding.eTime.text = it.data?.time


//                        binding.etCakeName.setText(it.data?.username)
//                        binding.etPriceN.setText(it.data?.phone)
//                        binding.etPriceName.setText(it.data?.email)
                    }
                    Status.ERROR ->{
                        binding.progressBar.visibility = View.GONE
                        snackbar(it.message.toString())
                    }
                    Status.LOADING ->{ binding.progressBar.visibility = View.VISIBLE}
                }
            }

        })
    }


}





























