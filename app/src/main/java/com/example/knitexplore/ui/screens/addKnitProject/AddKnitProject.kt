package com.example.knitexplore.ui.screens.addKnitProject


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.knitexplore.R
import com.example.knitexplore.ui.theme.softerOrangeColor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddKnitProject (navController: NavHostController) {

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    Log.d("!!!", "currentUser ${currentUser?.uid}")



    val viewModel : AddKnitProjectViewModel = viewModel()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                Log.d("!!!", "Selected URI: $it")
                viewModel.setImageUri(it)
            }
        }
    )

    fun logInAnonymously () {
        auth.signInAnonymously()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "signInAnonymously:success")
                    val user = auth.currentUser
                    Log.d("!!!", "uid: ${user?.uid}")
                } else {
                    Log.w("!!!", "signInAnonymously:failure", task.exception)
                }
            }
    }

    if (currentUser == null) {
        logInAnonymously()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                })
        }
    ) { innerPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                Title()
            }
            item {
                Row(
                    modifier = Modifier.padding(10.dp),
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
                TextInput(
                    viewModel = viewModel,
                    value = viewModel.projectName,
                    label = "Enter your project",
                    onValueChange = { newText -> viewModel.projectName = newText },
                    onNext = {}
                )
            }

            item {
                TextInput(
                    viewModel = viewModel,
                    value = viewModel.patternName,
                    label = "Enter pattern name",
                    onValueChange = { newText -> viewModel.patternName = newText },
                    onNext = {}
                )
            }

            item {
                repeat(viewModel.numberOfNeedleSizes) {

                    val text = rememberSaveable  { mutableDoubleStateOf(0.0) }
                    TextInput(viewModel = viewModel,
                        value = text.doubleValue.toString(),
                        label = "Needle size",
                        onValueChange = { newValue ->
                            // if (newValue.matches(Regex("^\\d{0,2}(\\.\\d)?\$"))) {
                            if (newValue.matches(Regex("^\\d{0,2}(\\.\\d{0,1})?\\d?\$"))) {
                                // TODO Review regex, doesn't work to change the number after the decimal
                                text.doubleValue = newValue.toDouble()
                            }
                        },

                        onNext = { viewModel.addNeedleSize(text.doubleValue.toString()) })
                }
            }

            item {

                // TODO Check if the user has saved the previous needle sizes, otherwise the button should not be enabled
                AddBtnWithIcon(btnText = "Add needle") {
                    viewModel.increaseNumberNeedleSizes()
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
                repeat(viewModel.numberOfYarns) {
                    val text = rememberSaveable { mutableStateOf("") }
                    TextInput(
                        viewModel = viewModel,
                        value = text.value,
                        label = "Enter yarn you used",
                        onValueChange = { newText -> text.value = newText },
                        onNext = {
                            viewModel.addYarn(text.value)
                            Log.d("!!!", "Nästa rad körs, yarn")
                        })
                }
            }
            item {
                // TODO Check if the user has saved the previous yarn, otherwise the button should not be enabled

                AddBtnWithIcon(btnText = "Add yarn") {
                    viewModel.increaseNumberYarns()
                }
            }

            item {
                ProjectNotesInput(notes = viewModel.projectNotes,
                    onNotesChange = {
                        viewModel.projectNotes = it
                    })
            }

            item {
                SaveBtn(viewModel = viewModel)
            }
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
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
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GaugeInput(
            value = if (viewModel.stitchesAmount == 0) "" else viewModel.stitchesAmount.toString(),
            onValueChange = { newText ->
                viewModel.stitchesAmount = if (newText.isEmpty()) 0 else newText.toInt()
            }
        )

        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "Stitches in")
        Spacer(modifier = Modifier.width(5.dp))
        GaugeInput(
            value = viewModel.rowsAmount.toString(),
            onValueChange = {newText -> viewModel.rowsAmount = newText.toInt()}
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "rows")
    }

}

@Composable
fun ProjectNotesInput(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    OutlinedTextField(
        value = notes,
        label = {Text("Project notes")},
        onValueChange = onNotesChange,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .heightIn(min = 100.dp),
        textStyle = TextStyle.Default.copy(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),

        modifier = Modifier
            .width(75.dp)
            .padding(10.dp)
        )
}



@Composable
fun AddBtnWithIcon (btnText: String, btnPressed: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 10.dp)


    ) {
        Box(modifier = Modifier.clickable {
            btnPressed()

        }) {
            Row {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = softerOrangeColor
                )
                Text(
                    text = btnText,
                    modifier = Modifier.padding(start = 10.dp))
            }
        }
    }
}

@Composable
fun SaveBtn (viewModel: AddKnitProjectViewModel) {

    Row {
        Button(
            onClick = { viewModel.saveImageToStorage() },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = softerOrangeColor
            ))
        {
            Text(text = "Save")
        }
    }
}

@Composable
fun TextInput (viewModel: AddKnitProjectViewModel, value: String, label: String, onValueChange: (String) -> Unit, onNext: () -> Unit) {

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
            keyboardOptions = KeyboardOptions(
                imeAction = if (label == "Project Notes" || label == "Needle size" || label == "Enter yarn you used") ImeAction.Done else ImeAction.Next,
                keyboardType = keyboardType),
            keyboardActions = KeyboardActions(

                onDone = {
                    onNext()
                        focusManager.clearFocus()
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
fun AddImageBox( imagePressedAction: () -> Unit) {

    Surface(
        modifier = Modifier
            .size(300.dp)
            .padding(8.dp)
            .clickable { imagePressedAction() },
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
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
        modifier = Modifier.padding(top = 10.dp, bottom = 40.dp))
    {
        Text(text = "Add new project",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }
}

