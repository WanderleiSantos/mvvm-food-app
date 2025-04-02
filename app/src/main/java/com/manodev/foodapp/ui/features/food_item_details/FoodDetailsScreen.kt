package com.manodev.foodapp.ui.features.food_item_details

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.manodev.foodapp.R
import com.manodev.foodapp.data.models.FoodItem
import com.manodev.foodapp.ui.BasicDialog
import com.manodev.foodapp.ui.features.restaurants_details.RestaurantDetails
import com.manodev.foodapp.ui.features.restaurants_details.RestaurantDetailsHeader
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.FoodDetailsScreen(
    navController: NavController,
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: FoodDetailsViewModel = hiltViewModel(),
) {

    val showSuccessDialog = remember {
        mutableStateOf(false)
    }

    val showErrorDialog = remember {
        mutableStateOf(false)
    }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val count = viewModel.quantity.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        FoodDetailsViewModel.FoodDetailsUiState.Loading -> {
            isLoading.value = true
        }

        else -> {
            isLoading.value = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is FoodDetailsViewModel.FoodDetailsEvent.onAddToCart -> {
                    showSuccessDialog.value = true
                }

                is FoodDetailsViewModel.FoodDetailsEvent.showErrorDialog -> {
                    showErrorDialog.value = true
                }

                is FoodDetailsViewModel.FoodDetailsEvent.goToCart -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RestaurantDetailsHeader(
            imageUrl = foodItem.imageUrl,
            restaurantID = foodItem.id.toString(),
            animatedVisibilityScope = animatedVisibilityScope,
            onBackButtonClick = { navController.popBackStack() }
        ) {}

        RestaurantDetails(
            title = foodItem.name,
            description = foodItem.description,
            restaurantID = foodItem.id.toString(),
            animatedVisibilityScope = animatedVisibilityScope
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$${foodItem.price}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { viewModel.incrementQuantity() })
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "${count.value}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.size(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.minus), contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { viewModel.decrementQuantity() })
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                foodItem.id?.let {
                    viewModel.addToCart(
                        restaurantId = foodItem.restaurantId,
                        foodItemId = it
                    )
                }
            },
            enabled = !isLoading.value,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(32.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = !isLoading.value) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painter = painterResource(id = R.drawable.cart), contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Add to Cart".uppercase(Locale.ROOT), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                AnimatedVisibility(visible = isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }

    if (showSuccessDialog.value) {
        ModalBottomSheet(onDismissRequest = { showSuccessDialog.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Item added to cart", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = {
                        showSuccessDialog.value = false
                        viewModel.goToCart()
                    }, modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Go to Cart", style = MaterialTheme.typography.bodyMedium)
                }
                Button(
                    onClick = {
                        showSuccessDialog.value = false
                    }, modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Ok", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

    if (showErrorDialog.value) {
        ModalBottomSheet(onDismissRequest = { showErrorDialog.value = false }) {
            BasicDialog(
                title = "Error",
                description = (uiState.value as? FoodDetailsViewModel.FoodDetailsUiState.Error)?.message
                    ?: "Failed to add item to cart"
            ) {
                showErrorDialog.value = false
            }
        }
    }

}