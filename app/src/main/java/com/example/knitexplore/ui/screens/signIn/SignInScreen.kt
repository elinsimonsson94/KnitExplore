package com.example.knitexplore.ui.screens.signIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.R
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.components.LogoImage
import com.example.knitexplore.ui.components.PasswordInput
import com.example.knitexplore.ui.components.TextInput
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun SignInScreen(navController: NavHostController) {
    val viewModel: SignInViewModel = viewModel()
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
        LogInForm(viewModel = viewModel, navController)
        LogInBtn(viewModel = viewModel, navController)

        viewModel.toastMessage.observe(lifecycleOwner) { message ->
            if (!viewModel.showedToastMessage) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.showedToastMessage = true
            }
        }
    }
}

@Composable
fun LogInForm (viewModel: SignInViewModel, navController: NavHostController) {
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
        DoNotHaveAccountRow(navController = navController)
    }

}



@Composable
fun LogInBtn(viewModel: SignInViewModel, navController: NavHostController) {
    Row(
        modifier = Modifier.padding(top = 50.dp)
    ) {
        Button(
            onClick = {
                //viewModel.signIn(navController
                      viewModel.validateAndSignIn(navController)
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
fun DoNotHaveAccountRow(navController: NavHostController) {
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
                navController.navigate(NavigationItem.SignUp.route)
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



