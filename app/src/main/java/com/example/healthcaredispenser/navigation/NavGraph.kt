package com.example.healthcaredispenser.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthcaredispenser.ui.screens.SignupScreen
import com.example.healthcaredispenser.ui.screens.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val SIGNUP  = "signup"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onLoginClick = { _, _ -> /* TODO: 로그인 */ },
                onSignUpClick = { navController.navigate(Routes.SIGNUP) }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { nickname, email, password ->
                    // TODO: 회원가입 처리 후 pop 또는 다음 화면 이동
                }
            )
        }
    }
}
