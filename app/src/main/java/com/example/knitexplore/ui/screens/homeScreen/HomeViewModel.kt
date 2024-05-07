package com.example.knitexplore.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth


    fun checkIfLoggedIn(navController: NavHostController) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate(NavigationItem.AllKnitProjects.route)
        } else {
            navController.navigate(NavigationItem.SignIn.route)
        }
    }
}
