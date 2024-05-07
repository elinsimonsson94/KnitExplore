package com.example.knitexplore.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.knitexplore.R

@Composable
fun LogoImage () {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "null",
        Modifier.size(100.dp))
}