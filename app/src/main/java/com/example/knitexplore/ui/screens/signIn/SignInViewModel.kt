package com.example.knitexplore.ui.screens.signIn

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.data.NavigationItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    private val auth : FirebaseAuth = Firebase.auth

    fun signIn(navController: NavHostController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "signInWithEmail:success")
                    navController.navigate(NavigationItem.AllKnitProjects.route)
                } else {
                    Log.w("!!!", "signInWithEmail:failure", task.exception)
                }
            }
    }
}