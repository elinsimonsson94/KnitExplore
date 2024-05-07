package com.example.knitexplore.ui.screens.signIn

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.data.NavigationItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth

class SignInViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    val toastMessage = MutableLiveData<String>()
    var showedToastMessage by mutableStateOf(false)

    private val auth : FirebaseAuth = Firebase.auth

    fun validateAndSignIn(navController: NavHostController) {
        showedToastMessage = false // Resets showedToastMessage to false if the user has previously tried and failed to log in
        if (email != "" && password != "") {
            signIn(navController)
        } else {
            toastMessage.value = "You need to enter your Email and Password."
        }
    }

    fun signIn(navController: NavHostController) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(NavigationItem.AllKnitProjects.route)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Invalid email or password. Please try again."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password. Please try again."
                        else -> "Failed to sign in. Please try again later."
                    }
                    toastMessage.value = errorMessage
                    Log.w("!!!", "signInWithEmail:failure", task.exception)
                }
            }
    }
}