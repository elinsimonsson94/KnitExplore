package com.example.knitexplore.ui.screens.signIn

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
import com.example.knitexplore.ui.screens.addKnitProject.TextInput
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun SignInScreen(navController: NavHostController) {
    val viewModel: SignInViewModel = viewModel()

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
fun LogoImage () {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "null",
        Modifier.size(100.dp))
}

@Composable
fun LogInBtn(viewModel: SignInViewModel, navController: NavHostController) {
    Row(
        modifier = Modifier.padding(top = 50.dp)
    ) {
        Button(
            onClick = {
                viewModel.signIn(navController)
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

@Composable
fun PasswordInput(value: String, label: String, onValueChange: (String) -> Unit) {
    Row{
        TextField(
            value = value,
            onValueChange = { newText ->
                onValueChange(newText)
            },
            label = {
                Text(label)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
    }
}

