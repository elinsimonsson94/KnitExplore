package com.example.knitexplore.ui.screens.userPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.model.BottomNavItem
import com.example.knitexplore.ui.components.KnitProjectGridCell
import com.example.knitexplore.ui.shared.viewModels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen (navController: NavHostController, authViewModel: AuthViewModel) {
    val viewModel: UserViewModel = viewModel()
    viewModel.fetchKnitProjects()
    val userKnitProjects by viewModel.userKnitProjects.observeAsState(initial = emptyList())

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "My projects") },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.resetNavigation(navController)
                                authViewModel.logOut()
                            }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Log out Icon"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        if (userKnitProjects.isEmpty()) {
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
                itemsIndexed(userKnitProjects) { _, knitProject ->
                    KnitProjectGridCell(knitProject = knitProject) {
                        viewModel.setSelectedKnitProject(knitProject)

                        //navController.navigate(NavigationItem.KnitProjectDetails.route)
                        navController.navigate(BottomNavItem.KnitProjectDetails.route)
                    }
                }
            }
        }
    }
}
