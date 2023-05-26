package com.example.launder.ui.home.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.agent.R
import com.example.agent.databinding.FragmentHomeBinding
import com.example.agent.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingFragment :Fragment(R.layout.fragment_shopping){

    private lateinit var binding: FragmentShoppingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentShoppingBinding.bind(view)

    }



}
































