package com.example.launder.data

import com.google.firebase.auth.FirebaseUser
import android.net.Uri
import com.example.launder.data.entities.Service
import com.example.launder.data.entities.ProfileUpdate
import com.example.launder.data.entities.User

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun getUser(uid: String): Resouce<User>

    suspend fun updateProfilePicture(uid:String, imageUri: Uri):Uri?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun updateProfile(profileUpdate: ProfileUpdate):Resouce<Any>

    suspend fun deletePost(post: Service): Resouce<Service>

  //  suspend fun createPost(imageUri: Uri, name: String, prise:String,per:String): Resouce<Any>

//
//    suspend fun getPostsForProfile(uid: String):Resouce<List<Cake>>
    suspend fun signup(name: String, email: String, password: String,phone:String): Resource<FirebaseUser>
    fun logout()

    suspend fun createPot(imageUri: Uri, name: String, prise:String,per:String): Resouce<Any>
}