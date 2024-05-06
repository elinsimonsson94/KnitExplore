package com.example.knitexplore.ui.screens.knitProjectDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class KnitProjectDetailsViewModel : ViewModel() {
    var knitProjectViewModel = KnitProjectViewModel
    var selectedKnitProject = KnitProjectViewModel.getInstance()

    var auth = Firebase.auth
    val currentUser = auth.currentUser

    fun checkUid() {
        Log.d("!!!", "currentUser: ${currentUser?.uid}," +
                "selected uid: ${selectedKnitProject?.userUid}")
    }

    fun setEditAndNavigate(navController: NavHostController) {
        selectedKnitProject?.let {
            knitProjectViewModel.setSelectedKnitProject(it)
            navController.navigate(NavigationItem.AddKnitProject.route)
        }
    }
}