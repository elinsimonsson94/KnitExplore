package com.example.knitexplore.ui.screens.signUp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SignUpViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatPassword by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    val toastMessage = MutableLiveData<String>()
    var showedToastMessage by mutableStateOf(false)

    fun validateAndSignUp(navController: NavHostController) {
        showedToastMessage = false
        if (email != "" && password != "" && firstName != "" && lastName != "" && password == repeatPassword) {
            signInEmailAndPassword(navController)
        } else if (password != repeatPassword) {
            toastMessage.value = "Passwords do not match"
        } else {
            toastMessage.value = "You need enter all fields"
        }
    }

    private fun signInEmailAndPassword(navController: NavHostController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUid = auth.currentUser?.uid as String
                    val newUser = User(
                        userUid = userUid,
                        firstName = firstName,
                        lastName = lastName
                    )
                    saveUserData(newUser, navController)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Please enter a stronger password."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format. Please enter a valid email."
                        else -> "Failed to create the account. Please try again."
                    }

                    toastMessage.value = errorMessage
                    Log.w("!!!", "signUpWithEmail:failure", task.exception)
                }
            }
    }

    private fun saveUserData(user: User, navController: NavHostController) {
        val userRef = db.collection("users").document(user.userUid)

        userRef.set(user)
            .addOnSuccessListener {
                Log.d("!!!", "registered user successfully")
                navController.navigate(NavigationItem.AllKnitProjects.route)
            }
            .addOnFailureListener { e ->
                Log.w("!!!", "Error register the user: $e")
            }
    }
}