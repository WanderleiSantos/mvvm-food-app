package com.manodev.foodapp.ui.features.cart

import androidx.lifecycle.ViewModel
import com.manodev.foodapp.data.models.CartItem
import com.manodev.foodapp.data.models.CartResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CartEvent>()
    val event = _uiEvent.asSharedFlow()

    fun incrementQuantity(cartItem: CartItem, quantity: Int) {

    }

    fun decrementQuantity(cartItem: CartItem, quantity: Int) {

    }

    fun removeItem(cartItem: CartItem) {

    }

    fun checkout() {

    }

    sealed class CartUiState {
        data object Nothing : CartUiState()
        data object Loading : CartUiState()
        data class Success(val data: CartResponse) : CartUiState()
        data class Error(val message: String) : CartUiState()
    }

    sealed class CartEvent {
        data object showErrorDialog : CartEvent()
        data object onCheckout : CartEvent()
    }
}