package com.example.knitexplore.data

enum class Screen {
    HOME,
    ADD_KNIT_PROJECT,
    KNIT_PROJECT_DETAILS
}
sealed class NavigationItem (val route: String) {
    object Home : NavigationItem(Screen.HOME.name)

    object AddKnitProject : NavigationItem("Add_knit_project_screen/{isEditing}") {
        fun createRoute (isEditing: Boolean) : String {
            return "Add_knit_project_screen/$isEditing"
        }
    }
    object KnitProjectDetails : NavigationItem(Screen.KNIT_PROJECT_DETAILS.name)

}
