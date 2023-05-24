package com.example.launder.Auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.launder.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class RegisterActivity : ComponentActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonRegister.setOnClickListener {

        }
        binding.textViewLogin.setOnClickListener {

        }
    }
    private fun checkAllFields():Boolean{
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString()==""){
        return false
        }
        return false
    }
}

