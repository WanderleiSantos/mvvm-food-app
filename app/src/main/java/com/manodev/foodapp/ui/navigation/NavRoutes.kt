package com.manodev.foodapp.ui.navigation

import com.manodev.foodapp.data.models.FoodItem
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object AuthScreen

@Serializable
object Home

@Serializable
data class RestaurantDetails(
    val restaurantID: String,
    val restaurantName: String,
    val restaurantImageUrl: String
)

@Serializable
data class FoodDetails(
    val foodItem: FoodItem
)