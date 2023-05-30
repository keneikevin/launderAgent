package com.example.launder.data

import com.google.firebase.auth.FirebaseUser
import android.net.Uri
interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun createPost(imageUri: Uri, name: String, prise:String,per:String): Resouce<Any>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
    fun add()
}