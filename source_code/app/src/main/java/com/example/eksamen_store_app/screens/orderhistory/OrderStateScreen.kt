package com.example.eksamen_store_app.screens.orderhistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eksamen_store_app.R
import kotlin.random.Random

@Composable
fun OrderStateScreen(
    navController: NavController,

    ) {
    // random order number function
    val orderNumber = generateRandomOrderNumber()

    // get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = midnight)
            .padding(16.dp),
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
                modifier = Modifier.padding(16.dp),
                text = " Order confirmation",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
        Divider(color = lightPink)
        // main content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Thank you for your order! ❤️",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.paid_img),
                    contentDescription = "Order confirmation image",
                    modifier = Modifier
                        .scale(5.5f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your order number is: #${orderNumber},\nand will be delivered within 2-3 days.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(color = lightPink)
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("productListScreen") },
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
                    Text(text = "Continue Shopping", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

//generate a random order number
fun generateRandomOrderNumber(): Int {
    return Random.nextInt(100000, 999999)
}

@Preview(showBackground = true)
@Composable
fun OrderConfirmationPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        OrderStateScreen(navController = navController)
    }
}