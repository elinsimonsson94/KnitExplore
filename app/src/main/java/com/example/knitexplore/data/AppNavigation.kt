package com.example.knitexplore.data

enum class Screen {
    HOME,
    ALL_KNIT_PROJECTS,
    KNIT_PROJECT_DETAILS,
    SIGN_IN,
    SIGN_UP
}
sealed class NavigationItem (val route: String) {
    object Home : NavigationItem(Screen.HOME.name)

    object AllKnitProjects : NavigationItem(Screen.ALL_KNIT_PROJECTS.name)

    object AddKnitProject : NavigationItem("Add_knit_project_screen/{isEditing}") {
        fun createRoute (isEditing: Boolean) : String {
            return "Add_knit_project_screen/$isEditing"
        }
    }
    object KnitProjectDetails : NavigationItem(Screen.KNIT_PROJECT_DETAILS.name)

    object SignIn : NavigationItem(Screen.SIGN_IN.name)

    object SignUp : NavigationItem(Screen.SIGN_UP.name)

}
