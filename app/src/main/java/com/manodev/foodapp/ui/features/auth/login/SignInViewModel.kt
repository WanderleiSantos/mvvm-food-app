package com.manodev.foodapp.ui.features.auth.login

import androidx.lifecycle.viewModelScope
import com.manodev.foodapp.data.FoodApi
import com.manodev.foodapp.data.FoodHubSession
import com.manodev.foodapp.data.models.SignInRequest
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
class SignInViewModel @Inject constructor(
    override val foodApi: FoodApi,
    val session: FoodHubSession
) : BaseAuthViewModel(foodApi) {


    private val _uiState = MutableStateFlow<SignInEvent>(SignInEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignInNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading

            try {
                val response = safeApiCall {
                    foodApi.signIn(
                        SignInRequest(
                            email = email.value,
                            password = password.value
                        )
                    )
                }

                when (response) {
                    is ApiResponse.Success -> {
                        session.storeToken(response.data.token)
                        _uiState.value = SignInEvent.Success
                        _navigationEvent.emit(SignInNavigationEvent.NavigateToHome)
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
                        _uiState.value = SignInEvent.Error
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignInEvent.Error
            }

        }

    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignInNavigationEvent.NavigateToSignUp)
        }
    }

    sealed class SignInNavigationEvent {
        object NavigateToSignUp : SignInNavigationEvent()
        object NavigateToHome : SignInNavigationEvent()
    }

    sealed class SignInEvent {
        object Nothing : SignInEvent()
        object Success : SignInEvent()
        object Error : SignInEvent()
        object Loading : SignInEvent()
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            error = "Google Sign In Failed"
            errorDescription = msg
            _uiState.value = SignInEvent.Error
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            error = "Facebook Sign In Failed"
            errorDescription = msg
            _uiState.value = SignInEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = SignInEvent.Success
            _navigationEvent.emit(SignInNavigationEvent.NavigateToHome)
        }
    }
}