package com.example.knitexplore.ui.screens.addKnitProject


import android.net.Uri
import android.util.Log
import android.widget.ScrollView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.knitexplore.R

@Composable
fun AddKnitProject () {


    val viewModel : AddKnitProjectViewController = viewModel()

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                Log.d("!!!", "Selected URI: $it")
                viewModel.setImageUri(it)
            }
        }
    )

    LazyColumn(
        modifier= Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Title()
        }
        item {
            Row (modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (viewModel.imageUriState == null) {
                    AddImageBox {
                        singlePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }

                } else {
                    SelectedImage(uri = viewModel.imageUriState!!) {
                        singlePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
            }
        }
        item {
            SubTitle("Name your project")
        }
        item {
            ProjectNameInput(viewModel = AddKnitProjectViewController())
        }
        item {
            SaveBtn()
        }
        item {
            SubTitle("Name your project")
        }
    }
}

@Composable
fun SaveBtn () {
    Row {
        Button(onClick = { Log.d("!!!", "want to save") })
        {
            Text(text = "Save")
        }

    }
}

@Composable
fun SubTitle (text: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectNameInput (viewModel: AddKnitProjectViewController) {
    var projectName by remember { mutableStateOf(viewModel.projectName) }

    Row {

        TextField(
            value = projectName,
            onValueChange = { newText ->
                projectName = newText
                viewModel.projectName = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
    }
}
@Composable
fun AddImageBox(imagePressedAction: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(300.dp)
            .padding(8.dp)
            .clickable { imagePressedAction() },
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp // Skugga
    ) {
        Image(
            painter = painterResource(id = R.drawable.outline_image_24),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        )
    }
}


@Composable
fun SelectedImage (uri: Uri, onClickAction: () -> Unit) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(300.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClickAction()
            })
}

@Composable
fun Title () {

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 50.dp, bottom = 40.dp))
    {
        Text(text = "Add new project",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }


}