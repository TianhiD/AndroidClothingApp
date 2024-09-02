package com.example.eksamen_store_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.eksamen_store_app.data.room.ProductTypeConverter



@Entity(tableName = "products")
@TypeConverters(ProductTypeConverter::class)
data class Product(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating,
    val lastUpdated: Long
)

data class Rating(
    val rate: Double,
    val count: Int
)






