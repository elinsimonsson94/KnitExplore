package com.example.knitexplore.ui.screens.signIn

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
import com.example.knitexplore.ui.components.LogoImage
import com.example.knitexplore.ui.components.PasswordInput
import com.example.knitexplore.ui.components.TextInput
import com.example.knitexplore.ui.shared.viewModels.AuthViewModel
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun SignInScreen( viewModel: AuthViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LogoImage()
        WelcomeTitle()
        LogInForm(viewModel)
        LogInBtn(viewModel)

        viewModel.toastMessageLogIn.observe(lifecycleOwner) { message ->
            if (!viewModel.showedToastMessageLogIn) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.showedToastMessageLogIn = true
            }
        }
    }
}

@Composable
fun LogInForm (viewModel: AuthViewModel) {
    Column {
        TextInput(
            value = viewModel.email,
            label = "Enter your Email",
            onValueChange = {
                viewModel.email = it
            })
        PasswordInput(value = viewModel.password,
            label = "Enter your Password",
            onValueChange = {
                viewModel.password = it
            })
        DoNotHaveAccountRow(viewModel)
    }

}



@Composable
fun LogInBtn(authViewModel: AuthViewModel) {
    Row(
        modifier = Modifier.padding(top = 50.dp)
    ) {
        Button(
            onClick = {
                      authViewModel.validateAndSignIn()
                //viewModel.signIn(navController
                     // viewModel.validateAndSignIn(navController)
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
                "Log in",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DoNotHaveAccountRow(viewModel: AuthViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 10.dp)
    ) {
        Text(text = "Don't have an account? ")
        Text(text = "Sign up",
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
fun WelcomeTitle() {
    Row (
        modifier = Modifier.padding(bottom = 50.dp)
    ) {
        Text(text = "Welcome back!", fontSize = 25.sp)
    }
}



