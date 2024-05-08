package com.example.knitexplore.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.knitexplore.data.BottomNavItem
import com.example.knitexplore.ui.screens.addKnitProject.AddKnitProject
import com.example.knitexplore.ui.screens.allKnitProjects.AllKnitProjects
import com.example.knitexplore.ui.screens.knitProjectDetails.KnitProjectDetailsScreen
import com.example.knitexplore.ui.screens.userPage.UserScreen
import com.example.knitexplore.ui.shared.viewModels.AuthViewModel

@Composable
fun BottomNavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route) {

        // these are visible in bottomBar
        composable(BottomNavItem.Home.route) {
            AllKnitProjects(navController = navController)
        }
        composable(BottomNavItem.UserPage.route) {
            UserScreen(navController, authViewModel)
        }
        composable(BottomNavItem.AddKnitProject.route) {
            val isEditing = it.arguments?.getString("isEditing")?.toBoolean() ?: false
            AddKnitProject(navController = navController, isEditing = isEditing )
        }

        // this isn't visible
        composable(BottomNavItem.KnitProjectDetails.route) {
            KnitProjectDetailsScreen(navController = navController)
        }
    }
}