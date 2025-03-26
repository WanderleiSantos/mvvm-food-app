package com.manodev.foodapp.ui.features.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.manodev.foodapp.R
import com.manodev.foodapp.data.models.Category
import com.manodev.foodapp.data.models.Restaurant
import com.manodev.foodapp.ui.navigation.RestaurantDetails
import com.manodev.foodapp.ui.theme.Orange
import com.manodev.foodapp.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                is HomeViewModel.HomeScreenNavigationEvents.NavigateToDetail -> {
                    navController.navigate(RestaurantDetails(it.id, it.name, it.imageUrl))
                }

                else -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is HomeViewModel.HomeScreenstate.Loading -> {
                Text(text = "Loading")
            }

            is HomeViewModel.HomeScreenstate.Empty -> {
                Text(text = "Empty")
            }

            is HomeViewModel.HomeScreenstate.Success -> {
                val categories = viewModel.categories
                CategoriesList(categories) {
                    navController.navigate("detail/${it.id}")
                }
                RestaurantsList(restaurants = viewModel.restaurant, animatedVisibilityScope, onRestaurantSelected = {
                    viewModel.onRestaurantSelected(it)
                    //navController.navigate("restaurant/${it.id}")
                })
            }

        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantsList(
    restaurants: List<Restaurant>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRestaurantSelected: (Restaurant) -> Unit,
) {
    Column {
        Row {
            Text(
                text = "Popular Restaurants",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text(text = "View All", style = Typography.bodySmall)
            }
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    LazyRow {
        items(restaurants) {
            RestaurantItem(restaurant = it, animatedVisibilityScope, onRestaurantSelected)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantItem(
    restaurant: Restaurant,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRestaurantSelected: (Restaurant) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp)
            .height(229.dp)
            .shadow(16.dp, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/${restaurant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(12.dp)
                    .clickable { onRestaurantSelected(restaurant) }
            ) {
                Text(
                    text = restaurant.name,
                    style = Typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "title/${restaurant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                )

                Row(

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delivery),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp)
                                .size(12.dp)
                        )
                        Text(
                            text = "Free Delivery",
                            style = Typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.timer),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp)
                                .size(12.dp)
                        )
                        Text(
                            text = "Free Delivery",
                            style = Typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "4.5",
                style = Typography.titleSmall,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.size(4.dp))
            Image(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Yellow)
            )
            Text(
                text = "(25)",
                style = Typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun CategoriesList(categories: List<Category>, onCategorySelected: (Category) -> Unit) {
    LazyRow {
        items(categories) {
            CategoryItem(category = it, onCategorySelected)
        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategorySelected: (Category) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .height(90.dp)
            .width(60.dp)
            .clickable { onCategorySelected(category) }
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(45.dp),
                ambientColor = Color.Gray.copy(alpha = 0.8f),
                spotColor = Color.Gray.copy(alpha = 0.8f),
            )
            .background(color = Color.White)
            .clip(RoundedCornerShape(45.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Orange,
                    spotColor = Orange,
                )
                .clip(CircleShape),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = category.name,
            style = TextStyle(fontSize = 10.sp),
            textAlign = TextAlign.Center
        )
    }
}