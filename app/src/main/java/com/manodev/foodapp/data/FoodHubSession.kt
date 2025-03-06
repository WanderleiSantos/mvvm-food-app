package com.manodev.foodapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class FoodHubSession(
    context: Context,
) {

    private val sharedPres: SharedPreferences = context.getSharedPreferences("FoodHubSession", Context.MODE_PRIVATE)

    fun storeToken(token: String) {
        sharedPres.edit() { putString("token", token) }
    }

    fun getToken(): String? {
        sharedPres.getString("token", "")?.let {
            return it
        }
        return null
    }
}