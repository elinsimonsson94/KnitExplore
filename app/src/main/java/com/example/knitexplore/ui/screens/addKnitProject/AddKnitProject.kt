package com.example.knitexplore.ui.screens.addKnitProject


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.knitexplore.R
import kotlinx.coroutines.launch

@Composable
fun AddKnitProject () {


    val viewModel : AddKnitProjectViewModel = viewModel()
    val listState = rememberLazyListState()
    val needleSizes: MutableList<Double?> = MutableList(viewModel.numberOfNeedleSizes) { 0.0 }
    val myList: List<Int> = mutableListOf()
    val variableList: MutableList<MutableState<Int>> = mutableListOf()


    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                Log.d("!!!", "Selected URI: $it")
                viewModel.setImageUri(it)
            }
        }
    )

    fun addNewVariable() {
        variableList.add(mutableIntStateOf(0))
    }

    val coroutineScope = rememberCoroutineScope()


    var garnList by remember { mutableStateOf(mutableListOf("hej")) }

   /* Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        garnList.forEachIndexed { index, garn ->
            var text = remember { mutableStateOf("")}
            Log.d("!!!", "value: $garn")

            OutlinedTextField(
                value = text.value,
                onValueChange = { newGarn ->
                    Log.d("!!!", "newGarn: $newGarn")
                    text.value = newGarn
                },
                label = { Text("Garn") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = { *//* Perform action when Done button on keyboard is clicked *//* }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                garnList.add("")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Lägg till garn")
        }
    }*/



    LazyColumn(
        state = listState,
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

        }
        item {
            TextInput(
                viewModel = viewModel,
                value = viewModel.projectName,
                label = "Enter your project",
                onValueChange = { newText -> viewModel.projectName = newText},
                onDone = {}
            )
        }

        item {
            TextInput(
                viewModel = viewModel,
                value = viewModel.patternName,
                label = "Enter pattern name",
                onValueChange = { newText -> viewModel.patternName = newText },
                onDone = {}
            )
        }

        item {
            repeat(viewModel.numberOfNeedleSizes) {
                val text = remember { mutableDoubleStateOf(3.5) }
                TextInput(viewModel = viewModel,
                    value = text.doubleValue.toString(),
                    label = "Needle size",
                    onValueChange = {newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d?\$"))) {
                            text.doubleValue = newValue.toDouble()
                        } },
                    onDone = {viewModel.addNeedleSize(text.doubleValue.toString())})
            }
        }

        item {
          AddNeedleBtn(viewModel = viewModel) {
              /*coroutineScope.launch {
                  listState.scrollToItem(index = viewModel.numberOfNeedleSizes, scrollOffset = 50)
              }*/
          }
        }
        item {
            GaugeTitle()
        }
        item {
            GaugeInputs(viewModel = viewModel)
        }

        item {
            SaveBtn(viewModel = viewModel)
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun GarnTextField(
    garn: String,
    onGarnChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = garn,
        onValueChange = { newGarn ->
            onGarnChanged(newGarn)
        },
        label = { Text("Garn") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = { /* Perform action when Done button on keyboard is clicked */ }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
@Composable
fun GaugeTitle () {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, top = 35.dp, bottom = 10.dp))
    {
        Text(
            text = "Gauge 10x10 cm",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun GaugeInputs (viewModel: AddKnitProjectViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GaugeInput(
            value = viewModel.stitchesAmount,
            onValueChange = {newText -> viewModel.stitchesAmount = newText})
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "Stitches in")
        Spacer(modifier = Modifier.width(5.dp))
        GaugeInput(
            value = viewModel.rowsAmount,
            onValueChange = {newText -> viewModel.rowsAmount = newText})
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "rows")
    }

}

@Composable
fun GaugeInput (value: String,  onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
                        if (newValue.length <= 2) {
                            onValueChange(newValue)
                        }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(
            onDone =  {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .width(75.dp)
            .padding(10.dp)
        )
}


@Composable
fun AddNeedleBtn (viewModel: AddKnitProjectViewModel, scrollScreenDown: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 10.dp)


    ) {
        Box(modifier = Modifier.clickable {
            scrollScreenDown()
            viewModel.increaseNumberNeedleSizes()
        }) {
            Row {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = Color(0xFFA3D9A5))
                Text(
                    text = "Add needle",
                    modifier = Modifier.padding(start = 10.dp))
            }
        }
    }
}

@Composable
fun SaveBtn (viewModel: AddKnitProjectViewModel) {

    Row {
        Button(onClick = { viewModel.trySave() })
        {
            Text(text = "Save")
        }
    }
}

@Composable
fun TextInput (viewModel: AddKnitProjectViewModel, value: String, label: String, onValueChange: (String) -> Unit, onDone: () -> Unit) {

    val focusManager = LocalFocusManager.current

    var keyboardType: KeyboardType = KeyboardType.Text

    if (label == "Needle size") {
        keyboardType = KeyboardType.Number
    }


    Row {
        TextField(
            value = value,
            onValueChange = { newText ->
                            onValueChange(newText)
            },
            label = {
                    Text(label)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = keyboardType),
            keyboardActions = KeyboardActions(
                onDone =  {
                    focusManager.clearFocus()
                    onDone()
                }
            ),
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
        shadowElevation = 4.dp
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
            .size(350.dp)
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
