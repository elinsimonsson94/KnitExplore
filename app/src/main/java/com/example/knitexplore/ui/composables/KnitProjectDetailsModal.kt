package com.example.knitexplore.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.knitexplore.data.KnitProject

@Composable
fun KnitProjectDetailsModal (knitProject: KnitProject) {
    Box(modifier = Modifier.fillMaxWidth()
    ) {
        var imageIsLoading by remember { mutableStateOf(false) }
        AsyncImage(
            model = knitProject.imageUrl,
            contentDescription = "Knit Project image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f),

            onLoading = { imageIsLoading = true },
            onSuccess = { imageIsLoading = false }
        )
    }
}

