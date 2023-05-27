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
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment :Fragment(R.layout.fragment_map){

    private lateinit var binding: FragmentMapBinding
    lateinit var viewModel: AuthViewModel
    private var map:GoogleMap?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentMapBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

    binding.mapView.getMapAsync {
        map = it
    }

    }

    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }



}
































