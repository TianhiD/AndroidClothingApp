package com.example.eksamen_store_app.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteListViewModel : ViewModel() {
    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts = _favoriteProducts.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            val listOfFavoriteId = ShopRepository.getFavorites().map { it.productId }
            _favoriteProducts.value = ShopRepository.getProductsById(listOfFavoriteId)
        }
    }
}