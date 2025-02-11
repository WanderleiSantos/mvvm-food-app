package com.manodev.foodapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manodev.foodapp.ui.features.auth.AuthScreen
import com.manodev.foodapp.ui.features.auth.signup.SignUpScreen
import com.manodev.foodapp.ui.navigation.AuthScreen
import com.manodev.foodapp.ui.navigation.Home
import com.manodev.foodapp.ui.navigation.Login
import com.manodev.foodapp.ui.navigation.SignUp
import com.manodev.foodapp.ui.theme.MVVMFoodAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )
                zoomX.duration = 500
                zoomY.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMFoodAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AuthScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<SignUp> {
                            SignUpScreen(navController)
                        }
                        composable<AuthScreen> {
                            AuthScreen(navController)
                        }
                        composable<Login> {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Green)) {

                            }
                        }
                        composable<Home> {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)) {

                            }
                        }
                    }
                }

            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MVVMFoodAppTheme {
        Greeting("Android")
    }
}