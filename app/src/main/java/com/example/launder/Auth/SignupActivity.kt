package com.example.launder.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.launder.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class SignupActivity : ComponentActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivitySignupBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSSigned.setOnClickListener {
           // signUpUser()
        }
        binding.tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        fun signUpUser() {
            val email = binding.etSEmailAddress.text.toString()
            val pass = binding.etSPassword.text.toString()
            val confirmPassword = binding.etSConfPassword.text.toString()

            // check pass
            if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
                return
            }

            if (pass != confirmPassword) {
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            // If all credential are correct
            // We call createUserWithEmailAndPassword
            // using auth object and pass the
            // email and pass in it.
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

