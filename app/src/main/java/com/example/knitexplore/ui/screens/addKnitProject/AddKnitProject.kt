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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.knitexplore.model.NeedleYarnType
import com.example.knitexplore.ui.components.TextInput
import com.example.knitexplore.ui.theme.softerOrangeColor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddKnitProject(navController: NavHostController, isEditing: Boolean) {

    val auth = Firebase.auth
    val currentUser = auth.currentUser


    val viewModel: AddKnitProjectViewModel = viewModel()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    if (isEditing && !viewModel.fieldsUpdated) {
        viewModel.isEditing = true
        viewModel.updateFields()
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                Log.d("!!!", "Selected URI: $it")
                viewModel.setImageUri(it)
            }
        }
    )

    fun logInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.navigateBack(navController)
                    }) {
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
                Title(viewModel)
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
                    value = viewModel.projectName,
                    label = "Enter your project",
                    onValueChange = { newText -> viewModel.projectName = newText }
                )
            }

            item {
                TextInput(
                    value = viewModel.patternName,
                    label = "Enter pattern name",
                    onValueChange = { newText -> viewModel.patternName = newText }
                )
            }

            item {
                NeedleSizesInputs(viewModel = viewModel)
            }

            item {
                AddBtnWithIcon(btnText = "Add needle") {
                    viewModel.activateNextNeedle()
                }
            }
            item {
                GaugeTitle()
            }
            item {
                GaugeInputs(viewModel = viewModel)
            }

            item {
                YarnInputs(viewModel = viewModel)
            }
            item {
                AddBtnWithIcon(btnText = "Add yarn") {
                    viewModel.activateNextYarn()
                }
            }
            item {
                ProjectNotesInput(notes = viewModel.projectNotes,
                    onNotesChange = {
                        viewModel.projectNotes = it
                    })
            }
            item {
                SaveBtn(viewModel = viewModel, navController = navController)
                DeleteBtn(viewModel = viewModel, navController = navController)
            }
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun NeedleSizesInputs(viewModel: AddKnitProjectViewModel) {
    if (viewModel.needleVisibilityList[0]) {
        TextFieldWithClearIcon(
            viewModel = viewModel,
            isFirstField = true,
            value = viewModel.needle1,
            needleYarnType = NeedleYarnType.NEEDLE_SIZE,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d{0,2}(\\.\\d{0,1})?\\d?\$"))) {
                    viewModel.needle1 = newValue
                }
            },
        )
    }
    for (index in 1 until viewModel.needleVisibilityList.size) {
        if (viewModel.needleVisibilityList[index]) {
            val needleValue = when (index) {
                1 -> viewModel.needle2
                2 -> viewModel.needle3
                3 -> viewModel.needle4
                4 -> viewModel.needle5
                else -> 0.0
            }
            TextFieldWithClearIcon(
                viewModel = viewModel,
                isFirstField = false,
                value = needleValue.toString(),
                needleYarnType = NeedleYarnType.NEEDLE_SIZE,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d{0,2}(\\.\\d{0,1})?\\d?\$"))) {
                        when (index) {
                            1 -> viewModel.needle2 = newValue
                            2 -> viewModel.needle3 = newValue
                            3 -> viewModel.needle4 = newValue
                            4 -> viewModel.needle5 = newValue
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun YarnInputs(viewModel: AddKnitProjectViewModel) {
    if (viewModel.yarnVisibilityList[0]) {
        TextFieldWithClearIcon(
            needleYarnType = NeedleYarnType.YARN,
            viewModel = viewModel,
            isFirstField = true,
            value = viewModel.yarn1,
            onValueChange = { newValue ->
                viewModel.yarn1 = newValue
            },
        )
    }
    for (index in 1 until viewModel.yarnVisibilityList.size) {
        if (viewModel.yarnVisibilityList[index]) {
            val yarnValue = when (index) {
                1 -> viewModel.yarn2
                2 -> viewModel.yarn3
                3 -> viewModel.yarn4
                4 -> viewModel.yarn5
                else -> ""
            }
            TextFieldWithClearIcon(
                needleYarnType = NeedleYarnType.YARN,
                viewModel = viewModel,
                isFirstField = false,
                value = yarnValue,
                onValueChange = { newValue ->
                    when (index) {
                        1 -> viewModel.yarn2 = newValue
                        2 -> viewModel.yarn3 = newValue
                        3 -> viewModel.yarn4 = newValue
                        4 -> viewModel.yarn5 = newValue
                    }
                },
            )
        }
    }
}

@Composable
fun TextFieldWithClearIcon(
    viewModel: AddKnitProjectViewModel,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isFirstField: Boolean,
    needleYarnType: NeedleYarnType
) {

    var keyboardType = KeyboardType.Number
    if (needleYarnType == NeedleYarnType.YARN) {
        keyboardType = KeyboardType.Text
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            label = {
                Text(
                    if (needleYarnType == NeedleYarnType.NEEDLE_SIZE) {
                        "Needle size"
                    } else {
                        "Yarn"
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = keyboardType
            ),
            trailingIcon = {
                if (!isFirstField) {
                    IconButton(onClick = {
                        if (needleYarnType == NeedleYarnType.NEEDLE_SIZE) {
                            viewModel.deactivateCurrentNeedleField()
                        } else {
                            viewModel.deActivateCurrentYarn()
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Text"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun GaugeTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 35.dp, bottom = 10.dp)
    )
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
fun GaugeInputs(viewModel: AddKnitProjectViewModel) {
    Row(
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
            value = if (viewModel.rowsAmount == 0) "" else viewModel.rowsAmount.toString(),
            onValueChange = {
                newText -> viewModel.rowsAmount = if (newText.isEmpty()) 0 else newText.toInt()
            }
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
        label = { Text("Project notes") },
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
fun GaugeInput(value: String, onValueChange: (String) -> Unit) {
    Log.d("!!!", "gaugeInput: ")

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 2) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),

        modifier = Modifier
            .width(75.dp)
            .padding(10.dp)
    )
}

@Composable
fun AddBtnWithIcon(
    btnText: String,
    btnPressed: () -> Unit
) {
    Row(
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
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteBtn(viewModel: AddKnitProjectViewModel, navController: NavHostController) {
    if (viewModel.isEditing) {
        Row (
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.deleteKnitProjectData(navController)
                },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            )
            {
                Text(text = "Delete this project", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SaveBtn(viewModel: AddKnitProjectViewModel, navController: NavHostController) {

    Row (
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Button(
            onClick = {
                viewModel.saveOrUpdateFirebaseData(navController)
            },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = softerOrangeColor
            )
        )
        {
            Text(
                text = if (viewModel.isEditing) {
                    "Save changes"
                } else {
                    "Save"
                },
                fontSize = 16.sp
            )
        }
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
fun SelectedImage(uri: Uri, onClickAction: () -> Unit) {
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
fun Title(viewModel: AddKnitProjectViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 10.dp, bottom = 40.dp)
    )
    {
        Text(
            text = if (viewModel.isEditing) {
                "Edit your project"
            } else {
                "Add new project"
            },
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }
}

