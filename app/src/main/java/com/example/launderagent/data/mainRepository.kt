package com.example.launderagent.data

import com.google.firebase.auth.FirebaseUser
import android.net.Uri
import com.example.launderagent.data.entities.Order
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.ProfileUpdate
import com.example.launderagent.data.entities.User
import com.example.launderagent.other.Resouce
import com.example.launderagent.other.Resource

interface mainRepository {
    val currentUser: FirebaseUser?
    suspend fun getUser(uid: String): Resouce<User>

    suspend fun updateProfilePicture(uid:String, imageUri: Uri):Uri?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun updateProfile(profileUpdate: ProfileUpdate): Resouce<Any>

    suspend fun deleteService(post: Service): Resouce<Service>

  //  suspend fun createPost(imageUri: Uri, name: String, prise:String,per:String): Resouce<Any>



    suspend fun getServices(): Resouce<List<Service>>
    suspend fun signup(name: String, email: String, password: String,phone:String): Resource<FirebaseUser>
    fun logout()

    suspend fun createService(imageUri: Uri, name: String, prise:String, per:String): Resouce<Any>
    suspend fun bookServices(code: String,status:String,bookTime: String,completeTime: String, prise:String, services:List<Service>): Resouce<Any>

    suspend fun getOrder(uid: String): Resouce<Order>
}