package com.example.launder.data.entities
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cakes")
data class Cake (
    val mediaId: String ="",
    val img: String ="",
    @PrimaryKey()
    val price: String ="",
    val title: String ="",
    val per: String = ""
):Parcelable