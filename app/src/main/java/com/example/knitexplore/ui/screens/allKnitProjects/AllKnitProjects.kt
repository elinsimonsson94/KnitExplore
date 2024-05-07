package com.example.knitexplore.ui.screens.allKnitProjects

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.knitexplore.ui.theme.softerOrangeColor

@Composable
fun AllKnitProjects(navController: NavHostController) {
    val viewModel: AllKnitProjectsViewModel = viewModel()
    val knitProjects by viewModel.knitProjects.observeAsState(initial = emptyList())
    viewModel.fetchKnitProjects()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val knitProjectsTest by viewModel.knitProjectList.collectAsState()


    Scaffold(
        topBar = {
            SearchBar(
                searchText = searchText,
                onSearchTextChange = viewModel::onSearchTextChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.navigateToAddKnitProject(navController)
                    /* val isEditing = true
                     val route = NavigationItem.AddKnitProject.createRoute(isEditing = isEditing)
                     navController.navigate(route)*/
                },
                shape = CircleShape,
                containerColor = softerOrangeColor
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        val filteredProjects = if (searchText.isNotBlank()) {
            knitProjects.filter { project ->
                project.patternName.contains(searchText, ignoreCase = true)
            }
        } else {
            knitProjects
        }

        if (filteredProjects.isEmpty()) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No projects found",
                    modifier = Modifier.padding(16.dp)
                )
            }

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
                itemsIndexed(filteredProjects) { _, knitProject ->
                    KnitProjectGridCell(knitProject = knitProject) {
                        viewModel.setSelectedKnitProject(knitProject)

                        navController.navigate(NavigationItem.KnitProjectDetails.route)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChange("") },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.Gray
                    )
                }
            }
        },
        placeholder = {
            Text(
                "Search Pattern",
                color = Color.Gray,
                style = TextStyle(fontSize = 17.sp)
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .background(Color(0xFFECEFF1), RoundedCornerShape(40.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
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