package com.example.launder.ui.home.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment :Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.bind(view)

    }



}
































