package com.example.eksamen_store_app.screens.orderhistory

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.eksamen_store_app.R
import com.example.eksamen_store_app.data.History
import com.example.eksamen_store_app.data.ProductDetails



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel,
    onBackButtonClick: () -> Unit = {},
) {
    // get state from viewmodels
    val orderHistory by viewModel.orderHistory.collectAsState(emptyList())
    val productDetailsMap by viewModel.productDetailsMap.collectAsState(emptyMap())

    //get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))

    DisposableEffect(viewModel) {
        Log.d("OrderHistoryScreen", "ViewModel initialized: $viewModel")
        viewModel.loadHistoryForUser(userId = 1)
        Log.d("Compose", "Order history: $orderHistory")
        Log.d("Compose", "Product details map: $productDetailsMap")

        onDispose {
            Log.d("Compose", "DisposableEffect disposed")
        }
    }

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
                onClick = { onBackButtonClick() }
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
                text = "Order History♡",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.loadHistoryForUser(userId = 1)
                    }
                    .scale(2.5f)
                    .padding(start = 77.dp)
            )
        }

        Divider(color = lightPink)

        Column {

            if (orderHistory.isEmpty()) {
                Log.d("OrderHistoryViewModel", "Order history is empty")
            } else {
                OrderHistoryList(
                    orderHistory = orderHistory,
                    productDetailsMap = productDetailsMap,
                    addToCartFromOrderHistory = viewModel::addToCartFromOrderHistory
                )
            }
        }
    }
}

@Composable
fun OrderHistoryList(
    orderHistory: List<History>,
    productDetailsMap: Map<Int, ProductDetails>,
    addToCartFromOrderHistory: (History) -> Unit
) {
    LazyColumn{
        items(orderHistory) { history ->
            OrderHistoryItem(
                history = history,
                productDetailsMap = productDetailsMap,
                addToCartFromOrderHistory = addToCartFromOrderHistory
            )
        }
    }
}

@Composable
fun OrderHistoryItem(
    history: History,
    productDetailsMap: Map<Int, ProductDetails>,
    addToCartFromOrderHistory: (History) -> Unit
) {
    //get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)

        ) {

            Text(
                text = "Date ordered: ${history.date}",
                style = MaterialTheme.typography.headlineSmall,
                color = midnight
            )

            Spacer(modifier = Modifier.height(8.dp))

            history.products.forEachIndexed { index, orderProduct ->
                val productDetails = productDetailsMap[orderProduct.productId]

                if (productDetails != null) {
                    Log.d("OrderHistoryItem", "Product: ${productDetails.title}")
                    Text(text = "${index + 1}. ${productDetails.title}")
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            val totalPrice = history.products.sumOf { orderProduct ->
                val productDetails = productDetailsMap[orderProduct.productId]
                productDetails?.price ?: 0.0
            }
            val totalItems = history.products.sumOf { it.quantity }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Price: $$totalPrice \nItems: $totalItems")
                Spacer(modifier = Modifier.height(8.dp))


                Button(
                    onClick = { addToCartFromOrderHistory(history) },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Text(text = "ReOrder")
                }
                Log.d("ReOrder", "pressed on ReOrder button")
            }
        }
    }
}