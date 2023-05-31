package com.example.launder.data

import android.net.Uri
import com.example.launder.data.entities.Service
import com.example.launder.data.entities.ProfileUpdate
import com.example.launder.data.entities.User
import com.example.launder.data.other.Constants.DEFAULT_PROFILE_PICTURE
import com.example.launder.data.other.Constants.SERVICE_COLLECTION
import com.example.launder.data.other.safeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth

) : AuthRepository {
    var db = FirebaseFirestore.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private val cakes = firestore.collection(SERVICE_COLLECTION)

    private val users = firestore.collection("users")

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }
    }
    override suspend fun createPot(imageUri: Uri,name: String, prise:String,per:String) = withContext(Dispatchers.IO) {
        safeCall {
            val uid = firebaseAuth.uid!!
            val postId = UUID.randomUUID().toString()
            val imageUploadResult = storage.getReference(postId).putFile(imageUri).await()
            val imageUrl = imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()
            val post = Service(
                mediaId = postId,
                title = name,
                img = imageUrl,
                price = prise,
                per = per,
                authorUid = uid
            )
            cakes.document(postId).set(post).await()
            Resouce.success(Any())
        }
    }


    override suspend fun updateProfilePicture(uid: String, imageUri: Uri) = withContext(Dispatchers.IO) {

            val storageRef = storage.getReference(uid)
            val user = getUser(uid).data!!
            if (user.profilePictureUrl != DEFAULT_PROFILE_PICTURE) {
                storage.getReferenceFromUrl(user.profilePictureUrl).delete().await()
            }

            storageRef.putFile(imageUri).await().metadata?.reference?.downloadUrl?.await()
        }
    override suspend fun deletePost(post: Service) = withContext(Dispatchers.IO) {
        safeCall {
            cakes.document(post.mediaId).delete().await()
            storage.getReferenceFromUrl(post.img).delete().await()
            Resouce.success(post)
        }
    }


    override suspend fun updateProfile(profileUpdate: ProfileUpdate) = withContext(Dispatchers.IO) {
        safeCall {
//            val imageUrl = profileUpdate.profilePictureUri?.let { uri ->
//                updateProfilePicture(profileUpdate.uidToUpdate, uri).toString()
//            }

            val uid = firebaseAuth.uid!!
            val postId = UUID.randomUUID().toString()
            val imageUploadResult =
                profileUpdate.profilePictureUri?.let { storage.getReference(postId).putFile(it).await() }
            val imageUrl = imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()


            val map = mutableMapOf(
                "username" to profileUpdate.username,
                "phone" to profileUpdate.phone,
                "email" to profileUpdate.email,
                "time" to profileUpdate.time,
            )
            imageUrl.let { url ->
                map["profilePictureUrl"] = url
            }

            users.document(profileUpdate.uidToUpdate).update(map.toMap()).await().also {
                FirebaseAuth.getInstance().currentUser?.updateEmail(profileUpdate.email)
            }
            Resouce.success(Any())
        }

    }
    override suspend fun getUser(uid: String) = withContext(Dispatchers.IO) {
        safeCall {
            val user = users.document(uid).get().await().toObject(User::class.java)
                ?: throw IllegalStateException()
            val currentUid = FirebaseAuth.getInstance().uid!!
            val currentUser = users.document(currentUid).get().await().toObject(User::class.java)
                ?: throw IllegalStateException()

            Resouce.success(user)
        }
    }

    override suspend fun signup(name: String, email: String, password: String,phone: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await().also {

                val uid = it.user?.uid!!
                val user = User(uid,email,name,"https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png","phone",phone)
                users.document(uid).set(user).await()

            }

            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }
    }
//    override suspend fun getPostsForProfile(uid: String) = withContext(Dispatchers.IO) {
//        safeCall {
//            val profilePosts = cakes.whereEqualTo("authorUid", uid)
//                .orderBy("date", Query.Direction.DESCENDING)
//                .get()
//                .await()
//                .toObjects(Cake::class.java)
//                .onEach { post ->
//                    val user = getUser(post.authorUid).data!!
//                    post.img = user.profilePictureUrl
//                    post.title = user.username
//                    post.price = uid in post.likedBy
//                }
//            Resouce.success(profilePosts)
//        }
//    }

    override fun logout() {
        firebaseAuth.signOut()
    }


}