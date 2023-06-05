package com.example.launderagent.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "shopping_items")
data class ShoppingItem(
    var name:String,
    var size:Int,
    var price:Float,
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null
)
