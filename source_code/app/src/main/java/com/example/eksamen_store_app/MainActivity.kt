package com.example.eksamen_store_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eksamen_store_app.data.ShopRepository
import com.example.eksamen_store_app.screens.favorites.FavoriteListScreen
import com.example.eksamen_store_app.screens.favorites.FavoriteListViewModel
import com.example.eksamen_store_app.screens.orderhistory.OrderHistoryScreen
import com.example.eksamen_store_app.screens.orderhistory.OrderHistoryViewModel
import com.example.eksamen_store_app.screens.orderhistory.OrderStateScreen
import com.example.eksamen_store_app.screens.product_details.ProductDetailsScreen
import com.example.eksamen_store_app.screens.product_details.ProductDetailsViewModel
import com.example.eksamen_store_app.screens.product_list.ProductListScreen
import com.example.eksamen_store_app.screens.product_list.ProductListViewModel
import com.example.eksamen_store_app.screens.shoppingcart.ShoppingCartScreen
import com.example.eksamen_store_app.screens.shoppingcart.ShoppingCartViewModel
import com.example.eksamen_store_app.ui.theme.EksamenstoreappTheme

class MainActivity : ComponentActivity() {
    // viewmodels
    private val _productListViewModel: ProductListViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _favoriteListViewModel: FavoriteListViewModel by viewModels()
    private val _shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val _orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Initializing the Database
        ShopRepository.initializeDatabase(applicationContext)

        // Setting up the navigation using compose
        setContent {
            EksamenstoreappTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "productListScreen"
                ) {
                    // ProductListScreen
                    composable(
                        route = "productListScreen"
                    ) {
                        ProductListScreen(
                            viewModel = _productListViewModel,
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            },
                            navigateToFavoriteList = { navController.navigate("favoriteListScreen") },
                            navigateToShoppingCart = { navController.navigate("shoppingCartScreen") },
                            navigateToOrderHistory = { navController.navigate("orderHistoryScreen") })
                    }
                    // ProductDetailsScreen
                    composable(
                        route = "productDetailsScreen/{productId}",
                        arguments = listOf(
                            navArgument(name = "productId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId") ?: -1

                        LaunchedEffect(productId) {
                            _productDetailsViewModel.setSelectedProduct(productId)
                        }

                        ProductDetailsScreen(
                            viewModel = _productDetailsViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                            navigateToShoppingCart = { navController.navigate("shoppingCartScreen") }
                        )
                    }
                    // FavoriteListScreen
                    composable(route = "favoriteListScreen") {
                        LaunchedEffect(Unit) {
                            _favoriteListViewModel.loadFavorites()
                        }

                        FavoriteListScreen(
                            viewModel = _favoriteListViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/${productId}")
                            }
                        )
                    }
                    // ShoppingCartScreen
                    composable(route = "shoppingCartScreen") {
                        LaunchedEffect(Unit) {
                            _shoppingCartViewModel.loadShoppingCart()
                        }
                        ShoppingCartScreen(
                            viewModel = _shoppingCartViewModel,
                            navController = navController,
                            navigateToOrderState = { navController.navigate("orderStateScreen")},
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/${productId}")
                            },
                        )
                    }
                    // OrderStateScreen
                    composable(route = "orderStateScreen") {
                        OrderStateScreen(navController = navController)
                    }
                    // OrderHistoryScreen
                    composable(route = "orderHistoryScreen") {
                        OrderHistoryScreen(
                            viewModel = _orderHistoryViewModel,
                            onBackButtonClick = { navController.popBackStack()}
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}