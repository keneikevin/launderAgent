package com.example.launder.data.entities

import com.example.launder.data.other.Constants.DEFAULT_PROFILE_PICTURE
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val profilePictureUrl: String = DEFAULT_PROFILE_PICTURE,
    val time: String = "",
    val phone: String = ""

)