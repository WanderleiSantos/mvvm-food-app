package com.manodev.foodapp.ui.features.auth

import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.manodev.foodapp.data.FoodApi
import com.manodev.foodapp.data.auth.GoogleAuthUiProvider
import com.manodev.foodapp.data.models.OAuthRequest
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(open val foodApi: FoodApi) : ViewModel() {

    private val googleAuthUiProvider = GoogleAuthUiProvider()
    private lateinit var callbackManager: CallbackManager

    abstract fun loading()
    abstract fun onGoogleError(msg: String)
    abstract fun onFacebookError(msg: String)
    abstract fun onSocialLoginSuccess(token: String)

    fun onFacebookClicked(context: ComponentActivity){
        initiateFacebookLogin(context)
    }

    fun onGoogleClicked(context: ComponentActivity){
        initiateGoogleLogin(context)
    }

    protected fun initiateGoogleLogin(context: ComponentActivity) {
        viewModelScope.launch {
            loading()

            val response = googleAuthUiProvider.signIn(
                context,
                CredentialManager.create(context)
            )

            if (response != null) {

                val request = OAuthRequest(
                    token = response.token,
                    provider = "google"
                )

                val res = foodApi.oAuth(request)
                if (res.token.isNotEmpty()) {
                    onSocialLoginSuccess(res.token)
                } else {
                    onGoogleError("Failed")
                }


            } else {
                onGoogleError("Failed")
            }
        }
    }

    protected fun initiateFacebookLogin(context: ComponentActivity) {
        loading()

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    viewModelScope.launch {
                        val request = OAuthRequest(
                            token = loginResult.accessToken.token,
                            provider = "facebook"
                        )

                        val res = foodApi.oAuth(request)

                        if (res.token.isNotEmpty()) {
                            onSocialLoginSuccess(res.token)
                        } else {
                            onFacebookError("Failed not Token")
                        }
                    }
                }

                override fun onCancel() {
                    onFacebookError("Cancelled")
                }

                override fun onError(exception: FacebookException) {
                    onFacebookError("Failed")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(
            context,
            callbackManager,
            listOf("public_profile", "email"),
        )
    }
}