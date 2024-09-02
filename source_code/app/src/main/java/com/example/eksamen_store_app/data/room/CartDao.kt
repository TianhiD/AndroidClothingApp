package com.example.eksamen_store_app.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eksamen_store_app.data.Cart
import com.example.eksamen_store_app.data.Product


@Dao
interface CartDao {
    @Query("SELECT * FROM ShoppingCart")
    suspend fun getAllCartItems(): List<Cart>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cart: Cart)

    @Delete
    suspend fun deleteCartItem(cartItem: Cart)

    @Query("DELETE FROM ShoppingCart")
    suspend fun emptyCart()
}
