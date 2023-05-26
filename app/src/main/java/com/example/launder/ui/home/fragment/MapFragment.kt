package com.example.launder.ui.home.fragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.agent.databinding.FragmentMapBinding
import com.example.agent.databinding.FragmentProfileBinding
import com.example.launder.MainActivity
import com.example.launder.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment :Fragment(R.layout.fragment_map){

    private lateinit var binding: FragmentMapBinding
    lateinit var viewModel: AuthViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentMapBinding.bind(view)

    }



}
































