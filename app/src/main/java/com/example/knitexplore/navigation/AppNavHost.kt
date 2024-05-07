package com.example.knitexplore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.screens.addKnitProject.AddKnitProject
import com.example.knitexplore.ui.screens.homeScreen.HomeScreen
import com.example.knitexplore.ui.screens.knitProjectDetails.KnitProjectDetailsScreen
import com.example.knitexplore.ui.screens.signIn.SignInScreen

@Composable
fun AppNavHost (
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.SignIn.route
   // startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(route = NavigationItem.AddKnitProject.route) {
            val isEditing = it.arguments?.getString("isEditing")?.toBoolean() ?: false
            AddKnitProject(navController = navController, isEditing = isEditing )
        }
        composable(NavigationItem.KnitProjectDetails.route) {
            KnitProjectDetailsScreen(navController)
        }
        composable(NavigationItem.SignIn.route) {
            SignInScreen(navController = navController)
        }
    }
}