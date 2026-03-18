package com.example.quiz_app_starter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quiz_app_starter.MainMenuScreen
import com.example.quiz_app_starter.presentation.QuestionScreen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen");
    object QuestionScreen : Screen("question_screen");
    object EndScreen : Screen("end_screen");
}

@Composable
fun Navigation(bestScore: Int, modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route){
            MainMenuScreen(navController = navController, bestScore = bestScore, modifier = modifier)
        }
        composable(Screen.QuestionScreen.route) {
            QuestionScreen(navController = navController)
        }
        composable(Screen.EndScreen.route) {
            QuestionScreen(navController = navController)
        }
    }}
