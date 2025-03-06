package com.manodev.foodapp.data

import com.manodev.foodapp.data.models.AuthResponse
import com.manodev.foodapp.data.models.CategoriesResponse
import com.manodev.foodapp.data.models.OAuthRequest
import com.manodev.foodapp.data.models.SignInRequest
import com.manodev.foodapp.data.models.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}