package com.example.healthcaredispenser.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthcaredispenser.ui.auth.AuthViewModel
import com.example.healthcaredispenser.ui.screens.*

object Routes {
    const val WELCOME = "welcome"
    const val SIGNUP  = "signup"
    const val PROFILE = "profile"
    const val PROFILE_ADD = "profile_add"
    const val HABITS  = "habits"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    // 👉 간단버전: Factory 없이 바로 생성 (AuthViewModel은 AndroidViewModel 상속)
    val vm: AuthViewModel = viewModel()
    val ui = vm.state.collectAsState()

    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onLoginClick = { email, pw ->
                    vm.login(email, pw)
                },
                onSignUpClick = { navController.navigate(Routes.SIGNUP) }
            )

            // 로그인 성공 시 PROFILE로 이동
            LaunchedEffect(ui.value.loggedIn) {
                if (ui.value.loggedIn) {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            }
            // ui.value.loading / ui.value.error는 해당 화면에서 표시해도 되고, 여기서 스낵바로 띄워도 OK
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { email, pw ->
                    vm.signUp(email, pw)
                }
            )

            // 회원가입 성공 시 PROFILE로 이동
            LaunchedEffect(ui.value.loggedIn) {
                if (ui.value.loggedIn) {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        composable(Routes.PROFILE_ADD) {
            ProfileAddScreen(navController)
        }

        composable(Routes.HABITS) {
            HabitsScreen(navController = navController)
        }
    }
}
