package com.example.eksamen_store_app.screens.shoppingcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ShoppingCartViewModel : ViewModel() {

    private val _shoppingCartProducts = MutableStateFlow<List<Product>>(emptyList())
    val shoppingCartProducts = _shoppingCartProducts.asStateFlow()

    init {
        loadShoppingCart()
    }

    fun loadShoppingCart() {
        viewModelScope.launch {
            val listOfShoppingCartId = ShopRepository.getShoppingCart().map { it.productId }
            _shoppingCartProducts.value = ShopRepository.getProductsById(listOfShoppingCartId)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            ShopRepository.removeFromCart(productId)
            loadShoppingCart()
        }
    }

    fun emptyCart() {
        viewModelScope.launch {
            ShopRepository.emptyCart()
            loadShoppingCart()
        }
    }
}