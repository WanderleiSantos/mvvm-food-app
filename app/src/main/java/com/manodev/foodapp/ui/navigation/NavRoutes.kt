package com.manodev.foodapp.ui.navigation

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