package com.example.eksamen_store_app.screens.shoppingcart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.eksamen_store_app.R
import com.example.eksamen_store_app.data.room.ProductTypeConverter.formatTotalPrice
import com.example.eksamen_store_app.screens.product_list.ProductItem


@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel,
    navigateToOrderState: () -> Unit = {},
    onProductClick: (productId: Int) -> Unit = {},
    navController: NavController
) {
    val shoppingCartProducts = viewModel.shoppingCartProducts.collectAsState()

    //get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(midnight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("productListScreen") }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Navigate back",
                    tint = lightPink,
                    modifier = Modifier
                        .scale(1.5f)
                )
            }

            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
        Divider(color = lightPink)

        // main content
        Text(
            text = "Products in your cart: ${shoppingCartProducts.value.size}",
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(shoppingCartProducts.value) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable { onProductClick(product.id) }
                    ) {
                        ProductItem(
                            product = product,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 59.dp, vertical = 4.dp),
                            showDeleteButton = true,
                            onProductClick = {onProductClick(product.id)},
                            onRemoveClick = { productId ->
                                viewModel.removeFromCart(productId)
                            }
                        )
                    }
                }
            }
        }
        // price and button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Divider(color = lightPink)

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Total price: $${formatTotalPrice(shoppingCartProducts.value.sumOf { it.price })}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Button(
                onClick = {
                    Log.d("PlaceOrderButton", "Place order button clicked")
                    navigateToOrderState()
                    //empty cart
                    viewModel.emptyCart()
                    Log.d("PlaceOrderButton", "Cart emptied")
                },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Text(text = "Place order", color = Color.White)
            }
        }
    }
}





