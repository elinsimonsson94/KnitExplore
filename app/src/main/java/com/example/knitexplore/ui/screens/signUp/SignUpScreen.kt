package com.example.knitexplore.ui.screens.signUp

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
import com.example.knitexplore.ui.screens.addKnitProject.TextInput
import com.example.knitexplore.ui.screens.signIn.LogoImage
import com.example.knitexplore.ui.screens.signIn.PasswordInput
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun SignUpScreen (navController: NavHostController) {
    val viewModel : SignUpViewModel = viewModel()
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
        RegisterForm(viewModel = viewModel, navController)
        SignUpBtn(viewModel = viewModel, navController)
    }
    viewModel.toastMessage.observe(lifecycleOwner) { message ->
        if (!viewModel.showedToastMessage) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.showedToastMessage = true
        }
    }
}

@Composable
fun AlreadyHaveAccount (navController: NavHostController) {
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
                navController.navigateUp()
            }
        )
    }
}

@Composable
fun SignUpBtn (viewModel: SignUpViewModel, navController: NavHostController) {
    Button(
        onClick = {
                  viewModel.validateAndSignUp(navController)
            //viewModel.signInEmailAndPassword(navController)
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
fun RegisterForm(viewModel: SignUpViewModel, navController: NavHostController) {
    Column {
        TextInput(
            value = viewModel.email,
            label = "Enter your email",
            onValueChange = { viewModel.email = it }
        )
        PasswordInput(
            value = viewModel.password ,
            label = "Enter password",
            onValueChange = { viewModel.password = it }
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
        AlreadyHaveAccount(navController = navController)
    }

}