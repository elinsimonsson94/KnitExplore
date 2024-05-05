package com.example.knitexplore.ui.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier



import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.knitexplore.data.KnitProject

@Composable
fun KnitProjectDetailsModal (knitProject: KnitProject) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxWidth()
            .height(400.dp) // Här kan du justera höjden på modalen
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize() // Fyll hela utrymmet inuti Box
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Här kan du justera höjden på bilden
                ) {
                    var imageIsLoading by remember { mutableStateOf(false) }
                    AsyncImage(
                        model = knitProject.imageUrl,
                        contentDescription = "Knit Project image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .aspectRatio(1f)
                            .align(Alignment.TopCenter), // Placera bilden högst upp

                        onLoading = { imageIsLoading = true },
                        onSuccess = { imageIsLoading = false }
                    )
                }
            }

            // Här kan du lägga till fler objekt i listan om du behöver
        }
    }
}




