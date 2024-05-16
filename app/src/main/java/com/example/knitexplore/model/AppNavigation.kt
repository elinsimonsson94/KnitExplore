package com.example.knitexplore.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(val route: String, val icon: ImageVector,val selectedIcon: ImageVector, val label: String) {
    object Home : BottomNavItem(
        "home",
        Icons.Outlined.Home,
        Icons.Default.Home,
        "Home")

    object UserPage : BottomNavItem(
        "user_page",
        Icons.Outlined.AccountCircle,
        Icons.Default.AccountCircle,
        "Profile")

    object AddKnitProject : BottomNavItem(
        "Add_knit_project_screen/{isEditing}",
        Icons.Outlined.Add,
        Icons.Default.Add,
        "Add") {
        fun createRoute (isEditing: Boolean) : String {
            return "Add_knit_project_screen/$isEditing"
        }
    }
    object KnitProjectDetails : BottomNavItem(
        "knit_project_details",
        Icons.Default.Details,
        Icons.Default.Details,
        "Details")

}
