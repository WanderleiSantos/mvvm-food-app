package com.manodev.foodapp.ui.features.auth.signup

import androidx.lifecycle.viewModelScope
import com.manodev.foodapp.data.FoodApi
import com.manodev.foodapp.data.FoodHubSession
import com.manodev.foodapp.data.models.SignUpRequest
import com.manodev.foodapp.data.remote.ApiResponse
import com.manodev.foodapp.data.remote.safeApiCall
import com.manodev.foodapp.ui.features.auth.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    override val foodApi: FoodApi,
    val session: FoodHubSession
) : BaseAuthViewModel(foodApi) {

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
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading

            try {
                val response = safeApiCall {
                    foodApi.signUp(
                        SignUpRequest(
                            name = name.value,
                            email = email.value,
                            password = password.value
                        )
                    )
                }
                when (response) {
                    is ApiResponse.Success -> {
                        session.storeToken(response.data.token)
                        _uiState.value = SignupEvent.Success
                        _navigationEvent.emit(SignupNavigationEvent.NavigateToHome)
                    }

                    else -> {
                        val err = (response as? ApiResponse.Error)?.code ?: 0
                        error = "Sign Up Failed"
                        errorDescription = "Failed to sign up"

                        when (err) {
                            401 -> {
                                error = "Invalid Credentials"
                                errorDescription = "Invalid request"
                            }
                        }

                        _uiState.value = SignupEvent.Error
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignupEvent.Error
            }

        }

    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignupNavigationEvent.NavigateToLogin)
        }
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

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            error = "Google Sign In Failed"
            errorDescription = msg
            _uiState.value = SignupEvent.Error

        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            error = "Facebook Sign In Failed"
            errorDescription = msg
            _uiState.value = SignupEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = SignupEvent.Success
            _navigationEvent.emit(SignupNavigationEvent.NavigateToHome)
        }
    }
}