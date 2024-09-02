package com.example.eksamen_store_app.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopService {

    @GET("products")
    suspend fun getAllProducts(
        @Query("skip") skip: Int,
    ): Response<List<Product>>


    @GET("carts/user/{userId}")
    suspend fun getOrderHistory(
        @Path("userId") userId: Int
    ): List<History>

}