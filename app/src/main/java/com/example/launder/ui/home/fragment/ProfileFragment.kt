package com.example.launder.ui.home.fragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.agent.databinding.FragmentProfileBinding
import com.example.launder.MainActivity
import com.example.launder.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment :Fragment(R.layout.fragment_profile){

    private lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: AuthViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentProfileBinding.bind(view)
    binding.btnAddShoppingItem.setOnClickListener {
        viewModel.logout()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }
    }



}
































