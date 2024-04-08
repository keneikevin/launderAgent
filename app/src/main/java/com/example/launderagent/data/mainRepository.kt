package com.example.launderagent.data

import com.google.firebase.auth.FirebaseUser
import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.launderagent.data.entities.Order
import com.example.launderagent.data.entities.OrderUpdate
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.ProfileUpdate
import com.example.launderagent.data.entities.ShoppingItem
import com.example.launderagent.data.entities.User
import com.example.launderagent.other.Resouce
import com.example.launderagent.other.Resource

interface mainRepository {
    val currentUser: FirebaseUser?
    suspend fun getUser(uid: String): Resouce<User>

    suspend fun updateProfilePicture(uid:String, imageUri: Uri):Uri?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun updateProfile(profileUpdate: ProfileUpdate): Resouce<Any>
    suspend fun updateOrder(profileUpdate: OrderUpdate): Resouce<Any>

    suspend fun deleteService(post: Service): Resouce<Service>
    suspend fun deleteOrder(post: Order): Resouce<Order>

  //  suspend fun createPost(imageUri: Uri, name: String, prise:String,per:String): Resouce<Any>

    suspend fun getOrders(): Resouce<List<Order>>

    suspend fun getServices(): Resouce<List<Service>>
    suspend fun getUsers(): Resouce<List<User>>
    suspend fun signup(name: String, email: String, password: String,phone:String): Resource<FirebaseUser>
    fun logout()

    suspend fun createService(imageUri: Uri, name: String, prise:String, per:String): Resouce<Any>
    suspend fun bookServices(code: String,status:String,bookTime: String,completeTime: String, prise:String): Resouce<Any>

    suspend fun getOrder(uid: String): Resouce<Order>
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>
}