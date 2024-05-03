package com.example.knitexplore.ui.screens.homeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import com.example.knitexplore.ui.theme.softerOrangeColor
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.composables.KnitProjectDetailsModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: HomeScreenViewModel = viewModel()
    val knitProjects by viewModel.knitProjects.observeAsState(initial = emptyList())
    viewModel.fetchKnitProjects()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "All knit projects") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationItem.AddKnitProject.route) },
                shape = CircleShape,
                containerColor = softerOrangeColor) {
                Icon(Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        if (knitProjects.isEmpty()) {
            Text(text = "Det verkar vara tomt här")
        } else {
            val gridState = rememberLazyGridState()

            LazyVerticalGrid(
                modifier = Modifier.padding(innerPadding),
                columns = GridCells.Fixed(2),
                state = gridState,
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                itemsIndexed(knitProjects) { _, knitProject ->
                    KnitProjectGridCell(knitProject = knitProject) {
                        showBottomSheet = true
                    }
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            sheetState = sheetState
                        ) {
                            KnitProjectDetailsModal(knitProject = knitProject)
                        }
                    }
                }
            }

        }
    }

}

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
        KnitProjectPatternName(patterName = knitProject.patternName)
    }

}

@Composable
fun KnitProjectPatternName(patterName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = patterName,
            style = TextStyle(
                fontSize = 18.sp,

                )
        )
    }
}

@Composable
fun KnitProjectImage(imageUrl: String) {
    var imageIsLoading by remember { mutableStateOf(false) }
    var imageLoadFailed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Knit Project image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f),

            onLoading = { imageIsLoading = true },
            onSuccess = { imageIsLoading = false },
            onError = { imageLoadFailed = true }
        )

        if (imageIsLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (imageLoadFailed) {
            Log.d("!!!", "fail to loading images")
        }
    }
}