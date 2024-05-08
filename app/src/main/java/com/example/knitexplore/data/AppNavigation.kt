package com.example.knitexplore.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Login
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")

    object UserPage : BottomNavItem("user_page", Icons.Default.Face, "Profile")

    object AddKnitProject : BottomNavItem("Add_knit_project_screen/{isEditing}", Icons.Default.Add, "Add") {
        fun createRoute (isEditing: Boolean) : String {
            return "Add_knit_project_screen/$isEditing"
        }
    }
    object KnitProjectDetails : BottomNavItem("knit_project_details", Icons.Default.Details, "Details")

}
