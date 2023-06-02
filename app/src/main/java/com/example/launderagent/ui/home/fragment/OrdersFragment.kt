package com.example.launderagent.ui.home.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.bumptech.glide.RequestManager
import com.example.agent.R
import com.example.agent.databinding.FragmentOrderBinding
import com.example.launderagent.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class OrdersFragment :Fragment(R.layout.fragment_order){

    private lateinit var binding: FragmentOrderBinding
    lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavHostController
    @Inject
    lateinit var glide: RequestManager
    protected open val uid:String
        get() = FirebaseAuth.getInstance().uid!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentOrderBinding.bind(view)
        // Get the NavHostController from the Composable function
        // Create the NavHostController
        navController = NavHostController(this.requireActivity())
        viewModel.loadProfile(uid)


    }



}
































