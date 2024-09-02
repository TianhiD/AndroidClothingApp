package com.example.eksamen_store_app.screens.product_list


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen_store_app.data.Product
import com.example.eksamen_store_app.data.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _categoryFilter = MutableStateFlow<String?>(null)
    val categoryFilter = _categoryFilter.asStateFlow()

    private val _filteredCategories = MutableStateFlow<List<String>>(emptyList())

    private var currentPage = 0
    private val pageSize = 20


    init {
        viewModelScope.launch {
            loadCategories()
        }
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true

            try {
                val newProducts = ShopRepository.getProducts(currentPage * pageSize)

                val uniqueNewProducts = newProducts.filter { newProduct ->
                    !_products.value.any { it.id == newProduct.id }
                }
                _products.value = _products.value + uniqueNewProducts
                currentPage++
            } catch (_: Exception) {

            } finally {
                _loading.value = false
            }

        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _loading.value = true

            try {
                val searchResults = ShopRepository.searchProducts(query)
                _products.value = searchResults

            } catch (e: Exception) {

            } finally {
                _loading.value = false
            }
        }
    }

    fun filterProductsByCategory(category: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _categoryFilter.value = category

                val newProducts = if (category.equals("All", ignoreCase = true) || category.isBlank()) {
                    ShopRepository.getProducts(currentPage * pageSize)
                } else {
                    ShopRepository.getProductsByCategory(category, currentPage * pageSize)
                }
                _products.value = newProducts
            } catch (e: Exception) {
                Log.e("filterProductsByCategory", "Error filtering products by category", e)

            } finally {
                _loading.value = false
            }
            Log.d("ProductListViewModel", "Selected Category: $category")
            Log.d("ProductListViewModel", "Number of products after filtering: ${_products.value.size}")

            updateFilteredCategories()
        }
    }

    private fun updateFilteredCategories() {
        val filteredCategories = if (_categoryFilter.value.isNullOrBlank()) {
            _categories.value
        } else {
            _categories.value.filter { it.contains(_categoryFilter.value!!, ignoreCase = true) }
        }
        _filteredCategories.value = filteredCategories
    }

    private suspend fun loadCategories() {
        try {
            val categories = ShopRepository.getCategories()
            _categories.value = categories
            _filteredCategories.value = categories
        } catch (e: Exception) {
            Log.e("loadCategories", "Error loading categories", e)
        }
    }


}