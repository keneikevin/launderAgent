package com.example.launderagent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.launderagent.data.entities.ShoppingItem


@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItemDatabase: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem:ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price)FROM shopping_items")
    fun observeTotalPrice(): LiveData<Float>

    @Query("SELECT (price*size) FROM shopping_items")
    fun observePrice(): LiveData<Float>

//    @Query("SELECT * FROM cakes WHERE mediaId = :noteID")
//        suspend fun getCakeById(noteID: String): Cake?

}