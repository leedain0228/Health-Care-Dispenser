package com.example.healthcaredispenser.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    // 오늘 개발용: 프로필부터 시작 (완성 후 WELCOME 으로 변경)
    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            // 기존 웰컴 화면. 필요 시 여기서 프로필로 이동 버튼 추가 가능
            WelcomeScreen(
                onLoginClick = { _, _ -> navController.navigate(Routes.PROFILE) },
                onSignUpClick = { navController.navigate(Routes.SIGNUP) }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { _, _ ->
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        composable(Routes.PROFILE_ADD) { ProfileAddScreen(navController) }

        composable(Routes.HABITS) {
            HabitsScreen(navController = navController)
        }
    }
}


