package com.example.eksamen_store_app.screens.product_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.eksamen_store_app.R
import com.example.eksamen_store_app.data.Product

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (productId: Int) -> Unit,
    modifier: Modifier,
    showDeleteButton: Boolean = false,
    showBuyButton: Boolean = false,
    onRemoveClick: (productId: Int) -> Unit = {}
) {
    // get color from colors.xml
    val sky = Color(ContextCompat.getColor(LocalContext.current, R.color.sky_blue))

    // Product Item
    Column(
        modifier = modifier
            .height(260.dp)
            .fillMaxHeight()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10)
            )
            .background(sky)
            .clickable {
                onProductClick(product.id)
            }
            .width(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Image
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(138.dp)
                .width(144.dp)
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight(),
                model = product.image,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                contentDescription = "Image of ${product.title}"
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Text
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = product.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Price
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Buy Button
            if(showBuyButton) {
                Button(
                    onClick = {
                        onProductClick(product.id)
                        Log.d("ProductItem", "Add to cart button clicked")
                    }) {

                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = Color.White,
                        modifier = Modifier
                            .scale(1.5f)
                    )
                }
            }
        }

        // Delete Button
        if (showDeleteButton) {
            Box(
                modifier = Modifier
                    .scale(2f)
                    .padding(8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                IconButton(
                    onClick = { onRemoveClick(product.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove from Cart",
                        tint = Color.Red,

                    )
                }
            }
        }
    }
}
