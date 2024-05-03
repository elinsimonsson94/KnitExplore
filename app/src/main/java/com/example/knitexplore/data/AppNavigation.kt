package com.example.knitexplore.data

enum class Screen {
    HOME,
    ADDKNITPROJECT
}
sealed class NavigationItem (val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object AddKnitProject : NavigationItem(Screen.ADDKNITPROJECT.name)

}
