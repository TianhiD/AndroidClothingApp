package com.example.eksamen_store_app.screens.orderhistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen_store_app.data.History
import com.example.eksamen_store_app.data.OrderProduct
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.ProductDetails
import com.example.eksamen_store_app.data.ShopRepository
import com.example.eksamen_store_app.data.room.HistoryDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel : ViewModel() {

    private val historyDao: HistoryDao
        get() = ShopRepository.getHistoryDao()

    private var _orderHistory = MutableStateFlow<List<History>>(emptyList())
    val orderHistory: StateFlow<List<History>> = _orderHistory.asStateFlow()

    private var _productDetailsMap = MutableStateFlow<Map<Int, ProductDetails>>(emptyMap())
    val productDetailsMap: StateFlow<Map<Int, ProductDetails>> = _productDetailsMap.asStateFlow()

    private val _cart = MutableStateFlow<List<OrderProduct>>(emptyList())
    val cart: StateFlow<List<OrderProduct>> = _cart.asStateFlow()

    init {
        viewModelScope.launch {
            loadHistoryForUser(userId = 1)
        }
    }

    fun addToCartFromOrderHistory(history: History) {
        val itemsToAddToCart = history.products.mapNotNull { orderProduct ->
            val productDetails = productDetailsMap.value[orderProduct.productId]
            productDetails?.let {
                OrderProduct(
                    productId = it.id,
                    title = it.title,
                    price = it.price,
                    image = it.image,
                    quantity = orderProduct.quantity
                )
            }
        }

        addToCart(itemsToAddToCart)
        Log.d("OrderHistoryViewModel", "Added to cart: $itemsToAddToCart")
    }

    private fun addToCart(products: List<OrderProduct>) {
        val updatedCart = _cart.value.toMutableList()
        for (product in products) {
            val existingProduct = updatedCart.find { it.productId == product.productId }

            if (existingProduct != null) {
                existingProduct.quantity += product.quantity
            } else {
                updatedCart.add(product)
            }
        }

        _cart.value = updatedCart
    }


    private fun productToProductDetails(product: Product?): ProductDetails {
        return if (product != null) {
            ProductDetails(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image
            )
        } else {
            ProductDetails(
                id = -1,
                title = "Not Found",
                price = 0.0,
                image = ""
            )
        }
    }

    private suspend fun updateProductDetailsMap(histories: List<History>) {
        val updatedMap = mutableMapOf<Int, ProductDetails>()
        histories.forEach { history ->
            try {
                val products = history.products
                val productDetails = products.firstOrNull()?.let { orderProduct ->
                    productToProductDetails(ShopRepository.getProductById(orderProduct.productId))
                }
                productDetails?.let {
                    updatedMap[history.id] = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _productDetailsMap.value = updatedMap
    }

    fun loadHistoryForUser(userId: Int) {
        viewModelScope.launch {
            try {
                Log.d("OrderHistoryViewModel", "Loading history for user: $userId")
                // Assuming user with userId = 1
                val orderHistory = historyDao.getAllHistoryItems()

                if (orderHistory.isEmpty()) {
                    Log.d("OrderHistoryViewModel", "Offline history is empty. Loading online history.")
                    val offlineOrderHistory = ShopRepository.getOrderHistory(userId = 1)
                    historyDao.insertHistoryItem(offlineOrderHistory)
                    _orderHistory.value = offlineOrderHistory
                    updateProductDetailsMap(offlineOrderHistory)
                } else {
                    Log.d("OrderHistoryViewModel", "Loaded history from database.")
                    _orderHistory.value = orderHistory
                    updateProductDetailsMap(orderHistory)
                }
                _orderHistory.value = historyDao.getAllHistoryItems()

                updateProductDetailsMap(historyDao.getAllHistoryItems())
            } catch (e: Exception) {
                Log.e("OrderHistoryViewModel", "Error loading order history", e)
            }
        }
    }
}
