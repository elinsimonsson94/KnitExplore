package com.example.knitexplore.ui.screens.allKnitProjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.model.BottomNavItem
import com.example.knitexplore.ui.components.KnitProjectGridCell

@Composable
fun AllKnitProjects(navController: NavHostController) {
    val viewModel: AllKnitProjectsViewModel = viewModel()
    viewModel.fetchKnitProjects()
    val knitProjects by viewModel.knitProjects.observeAsState(initial = emptyList())
    val searchText by viewModel.searchText.collectAsState()


    Scaffold(
        topBar = {
            SearchBar(
                searchText = searchText,
                onSearchTextChange = viewModel::onSearchTextChange
            )
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
                        navController.navigate(BottomNavItem.KnitProjectDetails.route)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
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

