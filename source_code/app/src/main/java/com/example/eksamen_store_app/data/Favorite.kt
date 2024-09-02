package com.example.eksamen_store_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ShopFavorite")
data class Favorite(
    @PrimaryKey
    val productId: Int
)
