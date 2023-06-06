package com.example.launderagent.data.entities
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "orders")
data class Order (
    @PrimaryKey()
    val code: String ="",
    val price: String ="",
    val orderUid: String ="",
    val bookTime: String ="",
    val completeTime: String ="",
    val status: String =""
):Parcelable