package com.example.knitexplore.ui.screens.signUp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import com.example.knitexplore.ui.components.LogoImage
import com.example.knitexplore.ui.components.PasswordInput
import com.example.knitexplore.ui.components.TextInput
import com.example.knitexplore.ui.shared.viewModels.AuthViewModel
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun SignUpScreen (authViewModel: AuthViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LogoImage()
        SignInTitle()
        RegisterForm(authViewModel)
        SignUpBtn( authViewModel)
    }
    authViewModel.toastMessage.observe(lifecycleOwner) { message ->
        if (!authViewModel.showedToastMessage) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            authViewModel.showedToastMessage = true
        }
    }
}

@Composable
fun AlreadyHaveAccount (viewModel: AuthViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Already have an account? ")
        Text(text = "Sign in here",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = softerOrangeColor),
            modifier = Modifier.clickable {
                viewModel.toggleSignUpScreenVisibility()
            }
        )
    }
}

@Composable
fun SignUpBtn (viewModel: AuthViewModel) {
    Button(
        onClick = {
                  viewModel.validateAndSignUp()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = softerOrangeColor
        )
    )
    {
        Text(
            "Sign up",
            fontSize = 16.sp
        )
    }
}

@Composable
fun SignInTitle() {
    Text(text = "Create an account", fontSize = 25.sp)
}

@Composable
fun RegisterForm( viewModel: AuthViewModel) {
    Column {
        TextInput(
            value = viewModel.emailSignUp,
            label = "Enter your email",
            onValueChange = { viewModel.emailSignUp = it }
        )
        PasswordInput(
            value = viewModel.passwordSignUp ,
            label = "Enter password",
            onValueChange = { viewModel.passwordSignUp = it }
        )
        PasswordInput(
            value = viewModel.repeatPassword,
            label = "Repeat the password",
            onValueChange = { viewModel.repeatPassword = it }
        )
        TextInput(
            value = viewModel.firstName,
            label = "Your first name",
            onValueChange = {
                viewModel.firstName = it }
        )
        TextInput(
            value = viewModel.lastName,
            label = "Your last name",
            onValueChange = { viewModel.lastName = it }
        )
        AlreadyHaveAccount(viewModel)
    }

}