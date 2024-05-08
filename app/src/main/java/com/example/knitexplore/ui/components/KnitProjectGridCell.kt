package com.example.knitexplore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knitexplore.data.KnitProject



@Composable
fun KnitProjectGridCell(knitProject: KnitProject, knitProjectPressed: () -> Unit) {

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable {
                knitProjectPressed()
            },
    ) {
        KnitProjectImage(imageUrl = knitProject.imageUrl)
        KnitPatternName(patterName = knitProject.patternName)
    }
}