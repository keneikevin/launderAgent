package com.example.launder.ui.home.fragment


import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.agent.databinding.FragmentProfileBinding
import com.example.launder.MainActivity
import com.example.launder.ui.auth.AuthViewModel
import com.example.launder.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment :Fragment(R.layout.fragment_profile){

    private lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavHostController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentProfileBinding.bind(view)
        // Get the NavHostController from the Composable function
        // Create the NavHostController
        navController = NavHostController(this.requireActivity())

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    HomeScreen(viewModel,navController)
                }
            }
        }
        binding.btnAddShoppingItem.visibility = View.GONE
    binding.btnAddShoppingItem.setOnClickListener {
        viewModel.logout()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }
    }



}
































