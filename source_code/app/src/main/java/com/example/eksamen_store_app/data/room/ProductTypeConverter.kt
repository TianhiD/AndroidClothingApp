package com.example.eksamen_store_app.data.room

import androidx.room.TypeConverter
import com.example.eksamen_store_app.data.OrderProduct
import com.example.eksamen_store_app.data.Rating
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ProductTypeConverter {
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): Date? {
        return value?.let {
            try {
                dateFormat.parse(it)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: Date?): String? {
        return date?.let {
            dateFormat.format(it)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromRatingString(value: String): Rating {
        return gson.fromJson(value, Rating::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun toRatingString(rating: Rating): String {
        return gson.toJson(rating)
    }

    @TypeConverter
    @JvmStatic
    fun fromOrderProductList(orderProductList: List<OrderProduct>): String {
        return  gson.toJson(orderProductList)
    }

    @TypeConverter
    @JvmStatic
    fun toOrderProductList(orderProductListString: String): List<OrderProduct> {
        val type = object : TypeToken<List<OrderProduct>>() {}.type
        return gson.fromJson(orderProductListString, type)
    }

    @TypeConverter
    @JvmStatic
    fun formatTotalPrice(totalPrice: Double): String {
        return String.format("%.2f", totalPrice)
    }
}