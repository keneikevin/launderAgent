package com.example.launder

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore
class MainActivity : ComponentActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

