package com.example.knitexplore.ui.screens.addKnitProject


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.knitexplore.R

@Composable
fun AddKnitProject () {

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

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

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Add new project")
        }
        Row (modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            if (viewModel.imageUriState == null) {
                Box(modifier = Modifier
                    .size(250.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable {
                        singlePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
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
        Row {
            Button(onClick = { viewModel.saveImageToStorage() })
            {
              Text(text = "Save")
            }

        }
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