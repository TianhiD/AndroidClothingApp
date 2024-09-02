package com.example.eksamen_store_app.screens.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen_store_app.data.Cart
import com.example.eksamen_store_app.data.Favorite
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _isInCart = MutableStateFlow(false)
    private val isInCart = _isInCart.asStateFlow()

    fun setSelectedProduct(productId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _selectedProduct.value = ShopRepository.getProductById(productId)
            _isFavorite.value = isCurrentProductFavorite()
            _loading.value = false
        }
    }

    fun updateFavorite(productId: Int) {
        viewModelScope.launch {
            if (isFavorite.value) {
                ShopRepository.removeFavorites(Favorite(productId))
            } else {
                ShopRepository.addFavorites(Favorite(productId))
            }

            _isFavorite.value = isCurrentProductFavorite()
        }
    }

    private suspend fun isCurrentProductFavorite(): Boolean {
        return ShopRepository.getFavorites().any { it.productId == selectedProduct.value?.id}
    }

    fun updateCart(productId: Int) {
        viewModelScope.launch {
            if (isInCart.value) {
                ShopRepository.removeFromCart(productId)
            } else {
                ShopRepository.addCart(Cart(productId))
            }
            _isInCart.value = isCurrentProductInCart()
        }
    }

    private suspend fun isCurrentProductInCart(): Boolean {
        return ShopRepository.getShoppingCart().any { it.productId == selectedProduct.value?.id }
    }
}