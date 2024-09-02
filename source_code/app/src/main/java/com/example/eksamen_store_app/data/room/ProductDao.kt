package com.example.eksamen_store_app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eksamen_store_app.data.Product


@Dao
interface ProductDao {
    @Query("SELECT * FROM Products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM Products WHERE id = :id")
    suspend fun getProductById(id: Int): Product?

    @Query("SELECT * FROM Products WHERE id IN (:ids)")
    suspend fun getProductsById(ids: List<Int>): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(products: List<Product>)

    @Query("DELETE FROM Products")
    suspend fun clearAllProducts()

    @Query("SELECT * FROM Products WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<Product>

    @Query("SELECT * FROM Products WHERE category = :category")
    suspend fun getProductsByCategory(category: String): List<Product>
}