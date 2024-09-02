package com.example.eksamen_store_app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eksamen_store_app.data.Favorite
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.Cart
import com.example.eksamen_store_app.data.History

@Database(
    entities = [Product::class, Favorite::class, Cart::class, History::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(ProductTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getCartDao(): CartDao
    abstract fun getHistoryDao(): HistoryDao

}