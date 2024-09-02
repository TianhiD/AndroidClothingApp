package com.example.eksamen_store_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingCart")
data class Cart(
    @PrimaryKey
    val productId: Int
)

