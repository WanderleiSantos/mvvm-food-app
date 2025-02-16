package com.manodev.foodapp.ui.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.manodev.foodapp.R
import com.manodev.foodapp.ui.GroupSocialButtons
import com.manodev.foodapp.ui.navigation.AuthScreen
import com.manodev.foodapp.ui.navigation.Home
import com.manodev.foodapp.ui.navigation.Login
import com.manodev.foodapp.ui.navigation.SignUp
import com.manodev.foodapp.ui.theme.Orange
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthScreenViewModel = hiltViewModel()) {
    val imageSize = remember {
        mutableStateOf(IntSize.Zero)
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is AuthScreenViewModel.AuthNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home) {
                        popUpTo(AuthScreen) {
                            inclusive = true
                        }
                    }
                }

                is AuthScreenViewModel.AuthNavigationEvent.NavigateToSignUp -> {
                    navController.navigate(SignUp)
                }

                is AuthScreenViewModel.AuthNavigationEvent.ShowErrorDialog -> {

                }
            }
        }
    }

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black
        ),
        startY = imageSize.value.height.toFloat() / 3,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .onGloballyPositioned {
                    imageSize.value = it.size
                }
                .alpha(0.6f)
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = brush)
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.skip), color = Orange)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                color = Color.Black,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.food_hub),
                color = Orange,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.food_hub_desc),
                color = Color.DarkGray,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GroupSocialButtons(viewModel = viewModel)

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    navController.navigate(SignUp)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(text = stringResource(id = R.string.sign_with_email), color = Color.White)
            }

            TextButton(
                onClick = {
                    navController.navigate(Login)
                }
            ) {
                Text(text = stringResource(id = R.string.alread_have_account), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(rememberNavController())
}