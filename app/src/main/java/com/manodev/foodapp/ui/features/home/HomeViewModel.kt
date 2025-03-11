package com.manodev.foodapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manodev.foodapp.data.FoodApi
import com.manodev.foodapp.data.models.Category
import com.manodev.foodapp.data.models.Restaurant
import com.manodev.foodapp.data.remote.ApiResponse
import com.manodev.foodapp.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenstate>(HomeScreenstate.Loading)
    val uiState: StateFlow<HomeScreenstate> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<HomeScreenNavigationEvents?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    var categories = emptyList<Category>()
    var restaurant = emptyList<Restaurant>()

    init {
        viewModelScope.launch {
            categories = getCategories()
            restaurant = getPopularRestaurants()

            if (categories.isNotEmpty() && restaurant.isNotEmpty()) {
                _uiState.value = HomeScreenstate.Success
            } else {
                _uiState.value = HomeScreenstate.Empty
            }
        }
    }

    private suspend fun getCategories(): List<Category> {
        var list = emptyList<Category>()

        val response = safeApiCall {
            foodApi.getCategories()
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data
            }

            else -> {
            }
        }

        return list
    }

    private suspend fun getPopularRestaurants(): List<Restaurant> {
        var list = emptyList<Restaurant>()
        val response = safeApiCall {
            foodApi.getRestaurants(40.712776, -74.005973)
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data
            }

            else -> {
            }
        }

        return list
    }

    sealed class HomeScreenstate {
        object Loading : HomeScreenstate()
        object Success : HomeScreenstate()
        object Empty : HomeScreenstate()
    }

    sealed class HomeScreenNavigationEvents {
        object NavigateToDetail : HomeScreenNavigationEvents()
    }
}