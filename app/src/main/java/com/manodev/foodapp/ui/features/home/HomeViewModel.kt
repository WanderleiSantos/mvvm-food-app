package com.manodev.foodapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manodev.foodapp.data.FoodApi
import com.manodev.foodapp.data.models.Category
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

    init {
        getCategories()
        getPopularRestaurants()
    }


    fun getCategories() {
        viewModelScope.launch {
            val response = safeApiCall {
                foodApi.getCategories()
            }
            when (response) {
                is ApiResponse.Success -> {
                    categories = response.data.data
                    _uiState.value = HomeScreenstate.Success
                }

                is ApiResponse.Error -> {
                    _uiState.value = HomeScreenstate.Empty
                }

                else -> {
                    _uiState.value = HomeScreenstate.Empty
                }
            }
        }
    }

    fun getPopularRestaurants() {

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