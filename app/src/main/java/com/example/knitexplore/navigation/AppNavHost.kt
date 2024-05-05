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

@Composable
fun AppNavHost (
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.AddKnitProject.route) {
            AddKnitProject(navController)
        }
        composable(NavigationItem.KnitProjectDetails.route) {
            KnitProjectDetailsScreen(navController)
        }
    }
}