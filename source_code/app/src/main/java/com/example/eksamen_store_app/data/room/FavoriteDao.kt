package com.example.eksamen_store_app.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.eksamen_store_app.data.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM ShopFavorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}