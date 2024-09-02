package com.example.eksamen_store_app.screens.product_list


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.example.eksamen_store_app.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit = {},
    navigateToFavoriteList: () -> Unit = {},
    navigateToShoppingCart: () -> Unit = {},
    navigateToOrderHistory: () -> Unit = {},
) {
    // get state from viewmodels
    val loading = viewModel.loading.collectAsState()
    val products by viewModel.products.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val categoryFilter by viewModel.categoryFilter.collectAsState()

    //get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))

    // Loading screen
    if (loading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = midnight)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.loadProducts()
                    }
                    .scale(2.5f)
                    .padding(start = 10.dp)
            )
           Spacer(modifier = Modifier
               .weight(0.2f))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(5f))

                // Order History
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navigateToOrderHistory() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.clock_rotate_left_solid),
                            contentDescription = "Order History",
                            tint = lightPink
                        )
                    }
                    Text(
                        text = "Order History",
                        color = White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Favorites
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navigateToFavoriteList() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Navigate to Favorites",
                            tint = lightPink
                        )
                    }
                    Text(
                        text = "Favorites",
                        color = White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Shopping Cart
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navigateToShoppingCart() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "View Cart",
                            tint = lightPink
                        )
                    }
                    Text(text = "Cart",
                        color = White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Spacer(modifier =  Modifier.height(15.dp))
        // Search field
        SearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = { newQuery ->
                searchQuery = newQuery
            },
            onSearchAction = {
                Log.d("SearchAction", "Search action triggered")
                viewModel.searchProducts(searchQuery)
            }
        )

        Spacer(modifier = Modifier.height(2.dp))
        // Category filter
        CategoryFilter(
            categoryFilter = categoryFilter ?: "",
            expanded = expanded,
            onCategoryFilterClick = { expanded = !expanded },
            onCategorySelected = { category ->
                viewModel.filterProductsByCategory(category)
            },
            categories = categories,
        )
        Divider( color = lightPink)

        Spacer(modifier = Modifier.height(8.dp))
        Column {
            // Products count
            Text(
                text = "Products: ${products.size}",
                style = MaterialTheme.typography.titleMedium,
                color = White
            )
        }
        // main content
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    showBuyButton = true,
                    onProductClick = {
                        onProductClick(product.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchAction: () -> Unit
){
    val searchJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            textStyle = MaterialTheme.typography.bodyMedium,

               modifier = Modifier
                    .padding(1.dp),
            onValueChange = {
                onSearchQueryChange(it)
            },
            label = {Text("Search", color = White)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = White,
                focusedBorderColor = White,
                unfocusedBorderColor = White,
            )
        )
        IconButton(
            onClick = {
                searchJob?.cancel()
                coroutineScope.launch {
                    delay(500)
                    onSearchAction()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = White
            )
        }
    }
}

@Composable
fun CategoryFilter(
    categoryFilter: String,
    expanded: Boolean,
    onCategoryFilterClick: () -> Unit,
    onCategorySelected: (String) -> Unit,
    categories: List<String>
) {

    //get color from colors.xml
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))

    Box(
        modifier = Modifier
            .padding(2.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        TextButton(
            onClick = { onCategoryFilterClick() },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "Choose category: ${categoryFilter.ifBlank { "All" }}",
                color = White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onCategoryFilterClick() },
            modifier = Modifier
                .align(Alignment.Center)
                .background(White)
                .border(3.dp, lightPink, MaterialTheme.shapes.extraSmall)
        ) {
            //categories options
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        onCategoryFilterClick()
                    }
                )
            }
            //All option
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onCategorySelected("All")
                    onCategoryFilterClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductOverviewPreview() {
}