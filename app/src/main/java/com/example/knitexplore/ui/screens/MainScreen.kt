package com.example.knitexplore.ui.screens

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.knitexplore.data.BottomNavItem
import com.example.knitexplore.navigation.BottomNavGraph
import com.example.knitexplore.ui.screens.signIn.SignInScreen
import com.example.knitexplore.ui.screens.signUp.SignUpScreen
import com.example.knitexplore.ui.shared.viewModels.AuthViewModel

@Composable
fun MainScreen(navController: NavHostController) {

    val viewModel : AuthViewModel = viewModel()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val showSignUpScreen by viewModel.showSignUpScreen.collectAsState()


    if (isLoggedIn) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) { _ ->
            BottomNavGraph(navController = navController, authViewModel = viewModel)
        }
    } else if (!showSignUpScreen) {
        SignInScreen(viewModel)
    } else {
        SignUpScreen(viewModel)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.AddKnitProject,
        BottomNavItem.UserPage
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var areTabsVisible by remember { mutableStateOf(true) }


    areTabsVisible = currentRoute != BottomNavItem.AddKnitProject.route

    val transition = updateTransition(targetState = areTabsVisible, label = "Transition")

    val bottomBarAlpha by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 300, easing = FastOutLinearInEasing)
            } else {
                tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            }
        }
    ) {
        if (it) 1f else 0f
    }

    // Visa bara BottomBar om areTabsVisible är sant
    if (bottomBarAlpha > 0) {
        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier.alpha(bottomBarAlpha)
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = navBackStackEntry?.destination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {

    BottomNavigationItem(
        label = {
            Text(text = screen.label)
        },

        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            if (screen.label == "Add") {
                val isEditing = false
                val route = BottomNavItem.AddKnitProject.createRoute(isEditing)
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            } else {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }

        },
        icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") }
    )


}