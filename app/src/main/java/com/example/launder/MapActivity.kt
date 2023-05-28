package com.example.launder

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agent.R
import com.example.agent.databinding.ActivityHomeBinding
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.example.agent.databinding.CardBinding
import com.example.launder.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: CardBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}