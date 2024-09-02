package com.example.eksamen_store_app.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.eksamen_store_app.data.room.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import android.database.sqlite.SQLiteConstraintException
import com.example.eksamen_store_app.data.room.HistoryDao

object ShopRepository {

    // Retrofit
    private val _httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    private val _retrofit =
        Retrofit.Builder()
            .client(_httpClient)
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Service to get products
    private val _shopService = _retrofit.create(ShopService::class.java)
    // Get database
    private lateinit var _appDatabase: AppDatabase

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "AppDatabase"
        ).fallbackToDestructiveMigration().build()
    }
    // Products
    suspend fun getProducts(skip: Int): List<Product> {
        try {

            val response = _shopService.getAllProducts(skip)

            return if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()

                val existingProducts = _appDatabase.getProductDao().getProductsById(productList.map { it.id })

                productList.forEach { product ->
                    val existingProduct = existingProducts.find { it.id == product.id}
                    if (existingProduct != null) {
                        _appDatabase.getProductDao().addProducts(listOf(product))
                    } else {
                        _appDatabase.getProductDao().addProducts(listOf(product))
                    }
                }
                productList
            } else {
                Log.e("getProducts", "Response is not successful: ${response.message()}")
                _appDatabase.getProductDao().getAllProducts()
            }
    } catch (e: UnknownHostException) {
        Log.e("getProducts", "Error: Unable to connect to the server.")
        return _appDatabase.getProductDao().getAllProducts()
    } catch (e: Exception) {
        Log.e("getProducts","Error getting products", e)
        return _appDatabase.getProductDao().getAllProducts()
    }
}
    // get product by id
    suspend fun getProductById(productId: Int): Product? {
        return _appDatabase.getProductDao().getProductById(productId)
    }

    suspend fun getProductsById(ids: List<Int>): List<Product>{
        return _appDatabase.getProductDao().getProductsById(ids)
    }

    //Search products
    suspend fun searchProducts(query: String): List<Product> {
        return _appDatabase.getProductDao().searchProducts(query)
    }

    // Categories
    suspend fun getProductsByCategory(category: String, skip: Int): List<Product> {
        try {
            val response = _shopService.getAllProducts(skip)

            return if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()

                val filteredProducts = if (category.isBlank()) {
                    productList
                } else {
                    productList.filter { it.category.equals(category, ignoreCase = true) }
                }

                _appDatabase.getProductDao().addProducts(filteredProducts)
                filteredProducts
            } else {
                Log.e("getProductsByCategory", "Response is not successful: ${response.message()}")
                _appDatabase.getProductDao().getProductsByCategory(category)
            }
        } catch (e: UnknownHostException) {
            Log.e("getProductsByCategory", "Error: Unable to connect to the server.")
            return  _appDatabase.getProductDao().getProductsByCategory(category)
        } catch (e: Exception) {
            Log.e("getProductsByCategory", "Error getting products by category", e)
            return _appDatabase.getProductDao().getProductsByCategory(category)
        }
    }

    suspend fun getCategories(): List<String> {
        return try {
            val products = getProducts(0)
            products.map { it.category }.distinct()
        } catch (e: Exception) {
            Log.e("getCategories", "Error getting categories", e)
            emptyList()
        }
    }

    // Favorites
    suspend fun getFavorites(): List<Favorite> {
        return _appDatabase.getFavoriteDao().getAllFavorites()
    }

    suspend fun addFavorites(favorite: Favorite) {
        return _appDatabase.getFavoriteDao().insertFavorite(favorite)
    }

    suspend fun removeFavorites(favorite: Favorite) {
        return _appDatabase.getFavoriteDao().deleteFavorite(favorite)
    }

    // Shopping cart
    suspend fun getShoppingCart(): List<Cart> {
       return _appDatabase.getCartDao().getAllCartItems()
    }

    suspend fun addCart(cart: Cart) {
        try {
            _appDatabase.getCartDao().insertCartItem(cart)
        } catch (e: SQLiteConstraintException) {

            Log.e("addCart", "Error inserting cart item: ${e.localizedMessage}")
        }
    }

    suspend fun removeFromCart(productId: Int) {
        return _appDatabase.getCartDao().deleteCartItem(Cart(productId))
    }

    suspend fun emptyCart() {
        _appDatabase.getCartDao().emptyCart()
    }

    // History
    fun getHistoryDao(): HistoryDao {
        if (!::_appDatabase.isInitialized) {
            throw UninitializedPropertyAccessException("Database not initialized. Call initializeDatabase() first.")
        }
        return _appDatabase.getHistoryDao()
    }

    suspend fun getOrderHistory(userId: Int): List<History> {
        return try {
            Log.d("ShopRepository", "Loading order history for userId: $userId")
            val localOrderHistory = _appDatabase.getHistoryDao().getAllHistoryItems()

            if (localOrderHistory.isEmpty()) {
                val remoteOrderHistory = _shopService.getOrderHistory(userId = 1)

                if (remoteOrderHistory.isEmpty()) {
                    _appDatabase.getHistoryDao().insertHistoryItem(remoteOrderHistory)

                }
                return remoteOrderHistory
            }
            localOrderHistory
        } catch (e: Exception) {
            Log.e("getOrderHistory", "Error getting order history", e)
            _appDatabase.getHistoryDao().getAllHistoryItems()
        }
    }

}