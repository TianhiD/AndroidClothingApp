package com.example.eksamen_store_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.eksamen_store_app.data.room.ProductTypeConverter
import java.util.Date

@Entity(tableName = "OrderHistory")
data class History(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    @ColumnInfo(name = "date")
    @TypeConverters(ProductTypeConverter::class)
    val date: Date,
    val products: List<OrderProduct>
)

data class OrderProduct(
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    var quantity: Int
)

data class ProductDetails(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String
)