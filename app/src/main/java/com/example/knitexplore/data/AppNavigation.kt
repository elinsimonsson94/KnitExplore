package com.example.knitexplore.data

enum class Screen {
    HOME,
    ADD_KNIT_PROJECT,
    KNIT_PROJECT_DETAILS
}
sealed class NavigationItem (val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object AddKnitProject : NavigationItem(Screen.ADD_KNIT_PROJECT.name)
    object KnitProjectDetails : NavigationItem(Screen.KNIT_PROJECT_DETAILS.name)

}
