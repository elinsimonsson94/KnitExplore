package com.example.knitexplore.ui.screens.knitProjectDetails

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.knitexplore.ui.theme.softerOrangeColor


@Composable
fun KnitProjectDetailsScreen(navController: NavHostController) {
    val viewModel: KnitProjectDetailsViewModel = viewModel()
    val selectedKnitProject by viewModel.selectedKnitProject.observeAsState(initial = null)

    viewModel.refresh()
    viewModel.listenToDocument()

    if (selectedKnitProject != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                LargeImage(url = selectedKnitProject!!.imageUrl)
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                {

                    val needleSizeAsString: List<String> =
                        selectedKnitProject!!.needleSizes.map { "$it mm" }
                    val gauge =
                        "${selectedKnitProject!!.stitches} sts in ${selectedKnitProject!!.rows} rows"

                    ProjectNameHeader(
                        patternName = selectedKnitProject!!.projectName,
                        viewModel = viewModel,
                        navController = navController
                    )
                    OwnerName(name = selectedKnitProject!!.ownerName)
                    DetailRow(key = "Pattern", value = selectedKnitProject!!.patternName)
                    DetailRowWithList(key = "Needle size", values = needleSizeAsString)
                    DetailRowWithList(key = "Yarns", values = selectedKnitProject!!.yarns)
                    DetailRow(key = "Gauge 10x10 cm", value = gauge)
                    ProjectNotesTitle()
                    ProjectNotesDetails(notes = selectedKnitProject!!.projectNotes)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                BackBtn(navController = navController)
            }
        }
    }
}

@Composable
fun LargeImage(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}

@Composable
fun ProjectNameHeader(
    patternName: String,
    viewModel: KnitProjectDetailsViewModel,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = patternName,
            modifier = Modifier.padding(vertical = 20.dp),
            style = TextStyle(
                fontSize = 25.sp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        viewModel.knitProjectInstance?.let { selectedKnitProject ->
            viewModel.currentUser?.uid?.let { currentUserUid ->
                if (currentUserUid == selectedKnitProject.userUid) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable {
                                viewModel.setEditAndNavigate(navController)
                            }
                    )
                }
            }
        }
    }
}


@Composable
fun OwnerName(name: String) {
    Row(
        modifier = Modifier.padding(bottom = 30.dp)
    ) {
        Text(
            text = "By $name",
            style = TextStyle(
                fontSize = 20.sp
            )
        )
    }

}

@Composable
fun DetailRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = key,
                modifier = Modifier.align(Alignment.CenterStart),
                style = TextStyle(
                    color = Color.Gray
                )
            )
        }
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun DetailRowWithList(key: String, values: List<String>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        values.forEachIndexed { index, value ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (index == 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = key,
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = TextStyle(
                                color = Color.Gray
                            )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = value,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun ProjectNotesTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Text(
            text = "Project Notes",
            fontSize = 19.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

}


@Composable
fun ProjectNotesDetails(notes: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = notes,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )
    }
}

@Composable
fun BackBtn(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = softerOrangeColor
            )
        ) {
            Text(text = "Back")
        }
    }
}









