package com.example.knitexplore.ui.shared.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knitexplore.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()
    private val _showSignUpScreen = MutableStateFlow(false)
    val showSignUpScreen = _showSignUpScreen.asStateFlow()

    // loginScreen variables
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    val toastMessageLogIn = MutableLiveData<String>()
    var showedToastMessageLogIn by mutableStateOf(false)

    // SignUpScreen variables
    var emailSignUp by mutableStateOf("")
    var passwordSignUp by mutableStateOf("")
    var repeatPassword by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    val toastMessage = MutableLiveData<String>()
    var showedToastMessage by mutableStateOf(false)

    init {
        checkIfLoggedIn()
    }

    fun logOut() {
        auth.signOut()

        viewModelScope.launch {
            _isLoggedIn.emit(false)
        }
    }

    fun toggleSignUpScreenVisibility() {
        viewModelScope.launch {
            _showSignUpScreen.emit(!_showSignUpScreen.value)
        }
    }
    private fun checkIfLoggedIn () {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            handleLogin()
        } else {
            logOut()
        }
    }
    private fun handleLogin() {
        viewModelScope.launch {
            _isLoggedIn.emit(true)
        }
        email = ""
        password = ""
    }

    private fun handleSignUp() {
        viewModelScope.launch {
            _isLoggedIn.emit(true)
            _showSignUpScreen.emit(false)
        }

        emailSignUp = ""
        passwordSignUp = ""
        repeatPassword = ""
        firstName = ""
        lastName = ""
    }

    fun validateAndSignIn() {
        showedToastMessageLogIn = false // Resets showedToastMessage to false if the user has previously tried and failed to log in
        if (email != "" && password != "") {
            signInWithEmailAndPassword()
        } else {
            toastMessageLogIn.value = "You need to enter your Email and Password."
        }
    }

    private fun signInWithEmailAndPassword() {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleLogin()
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Invalid email or password. Please try again."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password. Please try again."
                        else -> "Failed to sign in. Please try again later."
                    }
                    toastMessageLogIn.value = errorMessage
                    Log.w("!!!", "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun validateAndSignUp() {
        showedToastMessage = false
        if (emailSignUp != "" && passwordSignUp != "" && firstName != "" && lastName != ""
            && passwordSignUp == repeatPassword) {
            createAccountWithEmailAndPassword()
        } else if (passwordSignUp != repeatPassword) {
            toastMessage.value = "Passwords do not match"
        } else {
            toastMessage.value = "You need enter all fields"
        }
    }

    private fun createAccountWithEmailAndPassword() {
        auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUid = auth.currentUser?.uid as String
                    val newUser = User(
                        userUid = userUid,
                        firstName = firstName,
                        lastName = lastName
                    )
                    saveUserData(newUser)
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

    private fun saveUserData(user: User) {
        val userRef = db.collection("users").document(user.userUid)

        userRef.set(user)
            .addOnSuccessListener {
                Log.d("!!!", "registered user successfully")
                handleSignUp()
            }
            .addOnFailureListener { e ->
                Log.w("!!!", "Error register the user: $e")
            }
    }
}