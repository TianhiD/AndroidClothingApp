package com.example.eksamen_store_app.screens.product_details

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.eksamen_store_app.R

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    onBackButtonClick: () -> Unit = {},
    navigateToShoppingCart: () -> Unit = {}
)
{
    // get state from viewmodels
    val loading = viewModel.loading.collectAsState()
    val productState = viewModel.selectedProduct.collectAsState()
    val isFavorite = viewModel.isFavorite.collectAsState()

    // Add to cart button snackbar
    var isSnackbarVisible by remember { mutableStateOf(false) }

    //get color from colors.xml
    val midnight = Color(ContextCompat.getColor(LocalContext.current, R.color.midnight_blue))
    val lightPink = Color(ContextCompat.getColor(LocalContext.current, R.color.light_pink))


    if (loading.value) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val product = productState.value
    if (product == null) {
        Text(text = "Failed to get product details. Selected product object is NULL!")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(midnight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(midnight) ,
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
                text = "Product Details",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
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
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Divider(color = lightPink)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .size(400.dp)
                .clip(MaterialTheme.shapes.small)
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                ),
            model = product.image,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            contentDescription = "Image of ${product.title}"
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.updateFavorite(product.id)
                    if (isFavorite.value) {
                        Log.d("FavoriteButton", "Product ${product.title} removed from favorites")
                    } else {
                        Log.d("FavoriteButton", "Product ${product.title} added to favorites")
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Update Favorite",
                    tint = lightPink,
                    modifier = Modifier
                        .padding(16.dp)
                        .scale(4f)
                )
            }

            Spacer(modifier =  Modifier.width(30.dp))
            Text(
                text = " ${product.title}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                )
        }

        Button(
            onClick = {
                viewModel.updateCart(product.id)
                isSnackbarVisible = true
                Log.d("AddToCartButton", "Product ${product.title} added to cart")
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
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
            Text(
                text = "Add to Cart",
                color = Color.White,
            )
        }
        // Snackbar --> Display when product is added to cart
        if (isSnackbarVisible) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = Color.Blue)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    ),
                action = {
                    TextButton(
                        onClick = {
                            isSnackbarVisible = false
                        }
                    ) {
                        Text("Ok✨", color = Color.White)
                    }
                }
            ) {
                Text("Product added to cart: ${product.title} "  , color = Color.White)
            }
        }
        // Display Rating
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Rating: ${product.rating.rate}⭐ (${product.rating.count} reviews)",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Category: ${product.category}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.Absolute.Right
        ) {
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Product description: \n${product.description}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    ProductDetailsScreen(viewModel = ProductDetailsViewModel())
}

