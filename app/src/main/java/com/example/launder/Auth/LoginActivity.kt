package com.example.launder.Auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.launder.R
import com.google.firebase.firestore.FirebaseFirestore
class LoginActivity : ComponentActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

