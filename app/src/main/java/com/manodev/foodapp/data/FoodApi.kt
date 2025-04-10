package com.manodev.foodapp.data

import com.manodev.foodapp.data.models.AddToCartRequest
import com.manodev.foodapp.data.models.AddToCartResponse
import com.manodev.foodapp.data.models.AuthResponse
import com.manodev.foodapp.data.models.CategoriesResponse
import com.manodev.foodapp.data.models.FoodItemResponse
import com.manodev.foodapp.data.models.OAuthRequest
import com.manodev.foodapp.data.models.RestaurantsResponse
import com.manodev.foodapp.data.models.SignInRequest
import com.manodev.foodapp.data.models.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {

    @GET("/food")
    suspend fun getFood(): List<String>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>

    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): Response<RestaurantsResponse>

    @GET("/restaurants/{restaurantId}/menu")
    suspend fun getFoodItemForRestaurant(@Path("restaurantId") restaurantId: String): Response<FoodItemResponse>

    @POST("/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<AddToCartResponse>

    @GET("/cart")
    suspend fun getCart(): Response<AddToCartResponse>
}