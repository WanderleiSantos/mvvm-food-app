package com.manodev.foodapp.ui.features.auth.signup

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import com.manodev.foodapp.data.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val foodApi: FoodApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupEvent>(SignupEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignupNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onSignupClick() {
        _uiState.value = SignupEvent.Loading

        _uiState.value = SignupEvent.Success
        _navigationEvent.tryEmit(SignupNavigationEvent.NavigateToHome)
    }

    sealed class SignupNavigationEvent {
        object NavigateToLogin : SignupNavigationEvent()
        object NavigateToHome : SignupNavigationEvent()
    }

    sealed class SignupEvent {
        object Nothing : SignupEvent()
        object Success : SignupEvent()
        object Error : SignupEvent()
        object Loading : SignupEvent()
    }
}