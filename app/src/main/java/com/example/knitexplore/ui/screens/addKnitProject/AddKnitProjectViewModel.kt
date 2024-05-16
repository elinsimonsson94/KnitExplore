package com.example.knitexplore.ui.screens.addKnitProject

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.model.KnitProject
import com.example.knitexplore.model.User
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddKnitProjectViewModel : ViewModel() {
    var imageUriState by mutableStateOf<Uri?>(null)
    var projectName by mutableStateOf("")
    var patternName by mutableStateOf("")
    var needle1 by mutableStateOf("")
    var needle2 by mutableStateOf("")
    var needle3 by mutableStateOf("")
    var needle4 by mutableStateOf("")
    var needle5 by mutableStateOf("")
    private var currentNeedle by mutableIntStateOf(0) //array always starts on 0
    var needleVisibilityList by mutableStateOf(listOf(true, false, false, false, false))
    var yarn1 by mutableStateOf("")
    var yarn2 by mutableStateOf("")
    var yarn3 by mutableStateOf("")
    var yarn4 by mutableStateOf("")
    var yarn5 by mutableStateOf("")
    private var currentYarn by mutableIntStateOf(0) //array always starts on 0
    var yarnVisibilityList by mutableStateOf(listOf(true, false, false, false, false))

    var stitchesAmount by mutableIntStateOf(0)
    var rowsAmount by mutableIntStateOf(0)
    var projectNotes by mutableStateOf("")


    private var storageRef = Firebase.storage.reference
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private var knitProject = KnitProjectViewModel.getInstance()
    var oldKnitProject = knitProject
    private var knitProjectViewModel = KnitProjectViewModel
    var fieldsUpdated by mutableStateOf(false)
    var imageUpdated by mutableStateOf(false)
    var isEditing by mutableStateOf(false)
    var user : User? = null

    init {
        fetchUser()
    }


    fun navigateBack(navController: NavHostController) {
        if (isEditing) {
            knitProjectViewModel.setSelectedKnitProject(oldKnitProject!!)
        }
        navController.navigateUp()
    }

    fun updateFields() {
        imageUriState = knitProject?.imageUrl?.toUri()
        projectName = knitProject?.projectName.toString()
        patternName = knitProject?.patternName.toString()
        val needleSizes = knitProject?.needleSizes
        val yarns = knitProject?.yarns

        needleSizes?.forEachIndexed { index, size ->
            when (index) {
                0 -> needle1 = size.toString()
                1 -> {
                    needle2 = size.toString()
                    activateNextNeedle()
                }

                2 -> {
                    needle3 = size.toString()
                    activateNextNeedle()
                }

                3 -> {
                    needle4 = size.toString()
                    activateNextNeedle()
                }

                4 -> {
                    needle5 = size.toString()
                    activateNextNeedle()
                }
            }
        }
        knitProject?.stitches?.let { sts ->
            stitchesAmount = sts
        }
        knitProject?.rows?.let { rows ->
            rowsAmount = rows
        }
        yarns?.forEachIndexed { index, yarn ->
            when (index) {
                0 -> yarn1 = yarn
                1 -> {
                    yarn2 = yarn
                    activateNextYarn()
                }

                2 -> {
                    yarn3 = yarn
                    activateNextYarn()
                }

                3 -> {
                    yarn4 = yarn
                    activateNextYarn()
                }

                4 -> {
                    yarn5 = yarn
                    activateNextYarn()
                }
            }
        }
        projectNotes = knitProject?.projectNotes.toString()
        fieldsUpdated = true

        val newKnitProject = KnitProject(
            ownerName = "Elin",
            userUid = "currentUser.uid",
            imageUrl = "",
            projectName = projectName,
            patternName = patternName,
            needleSizes = listOf(),
            stitches = stitchesAmount,
            rows = rowsAmount,
            yarns = listOf(),
            projectNotes = projectNotes
        )
        knitProjectViewModel.setSelectedKnitProject(newKnitProject)
    }

    fun activateNextNeedle() {
        currentNeedle++
        if (currentNeedle < needleVisibilityList.size) {
            needleVisibilityList = needleVisibilityList.toMutableList().also {
                it[currentNeedle] = true
            }
        }
    }

    fun activateNextYarn() {
        currentYarn++
        if (currentYarn < yarnVisibilityList.size) {
            yarnVisibilityList = yarnVisibilityList.toMutableList().also {
                it[currentYarn] = true
            }
        }
    }

    fun deactivateCurrentNeedleField() {
        Log.d("!!!", "försöker ta bort index: $currentNeedle")
        if (currentNeedle <= needleVisibilityList.size) {
            Log.d("!!!", "if sats true")
            needleVisibilityList = needleVisibilityList.toMutableList().also {
                it[currentNeedle] = false
            }
        }

        when (currentNeedle) {
            1 -> needle2 = ""
            2 -> needle3 = ""
            3 -> needle4 = ""
            4 -> needle5 = ""
        }
        currentNeedle--
    }

    fun deActivateCurrentYarn() {
        if (currentYarn <= yarnVisibilityList.size) {
            yarnVisibilityList = yarnVisibilityList.toMutableList().also {
                it[currentYarn] = false
            }
        }

        when (currentYarn) {
            1 -> yarn2 = ""
            2 -> yarn3 = ""
            3 -> yarn4 = ""
            4 -> yarn5 = ""
        }
        currentYarn--
    }

    private fun createYarnList(): List<String> {
        val yarnList = mutableStateListOf<String>()

        listOf(yarn1, yarn2, yarn3, yarn4, yarn5).forEach { newValue ->
            if (newValue != "") {
                yarnList.add(newValue)
            }
        }
        return yarnList
    }

    private fun createNeedleList(): List<Double> {
        val needleValuesList = mutableStateListOf<Double>()

        listOf(needle1, needle2, needle3, needle4, needle5).forEach { newValue ->
            if (newValue != "") {
                try {
                    val value = newValue.toDouble()
                    needleValuesList.add(value)
                } catch (e: NumberFormatException) {
                    Log.d("!!!", "Error converting to Double")
                }
            }
        }
        return needleValuesList
    }

    fun setImageUri(uri: Uri) {
        imageUriState = uri
        imageUpdated = true
    }

    private fun saveKnitProjectToFirebase(imageUrl: String) {
        Log.d("!!!", "saveKnit körs")
        val currentUser = auth.currentUser
        val needleSizeList = createNeedleList()
        val yarnList = createYarnList()
        val userName = "${user?.firstName} ${user?.lastName}"

        currentUser?.let {
            val newKnitProject = KnitProject(
                ownerName = userName,
                userUid = currentUser.uid,
                imageUrl = imageUrl,
                projectName = projectName,
                patternName = patternName,
                needleSizes = needleSizeList,
                stitches = stitchesAmount,
                rows = rowsAmount,
                yarns = yarnList,
                projectNotes = projectNotes
            )

            db.collection("knitProjects")
                .add(newKnitProject)
                .addOnSuccessListener {
                    Log.d("!!!", "DocumentSnapshot successfully written")
                }
                .addOnFailureListener { error ->
                    Log.w("!!!", "Error writing document, ", error)
                }
        }
    }

    fun fetchUser () {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userUid = currentUser.uid

            db.collection("users").document(userUid)
                .get().addOnSuccessListener { snapshot ->

                    try {
                        val fetchedUser = snapshot.toObject(User::class.java)
                       if (fetchedUser != null) {
                           user = fetchedUser
                           Log.d("!!!", "user first name: ${user?.firstName}")
                       }
                    } catch (e: Exception) {
                        Log.w("!!!", "Error converting user data: $e")
                    }
                }
                .addOnFailureListener { e->
                    Log.d("!!!", "failed fetching userData: $e")
                }
        }
    }

    fun uploadImage( onSuccess: (String) -> Unit) {
        imageUriState?.let { uri ->
            val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val now = Date()
            val fileName = formatter.format(now)
            val newImageRef = storageRef.child("$fileName.jpg")

            val uploadTask = newImageRef.putFile(uri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                newImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result.toString()
                    onSuccess(downloadUrl)
                } else {
                    Log.d("!!!", "Failed to upload image: ${task.exception}")
                }
            }
        }
    }

    fun deleteOldImage() {
        val oldImageUrl = knitProject?.imageUrl.toString()
        if (oldImageUrl.isNotEmpty()) {
            val oldImageRef = Firebase.storage.getReferenceFromUrl(oldImageUrl)
            oldImageRef.delete()
                .addOnSuccessListener {
                    Log.d("!!!", "Old image deleted successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("!!!", "Failed to delete old image: $exception")
                }
        }
    }

    fun deleteKnitProjectData(navController: NavHostController) {
        deleteOldImage()
        val documentId = knitProject?.documentId as String

        val docRef = db.collection("knitProjects").document(documentId)

        docRef.delete()
            .addOnSuccessListener {
                Log.d("!!!", "knitProject successfully deleted")
                navController.navigateUp()
                navController.navigateUp()
            }
            .addOnFailureListener { e ->
                Log.w("!!!", "Error delete project: $e")
            }
    }


    fun saveOrUpdateFirebaseData () {

        if (isEditing && imageUpdated) {
            Log.d("!!!", "editing och ny image")
            uploadImage() { downloadUrl ->
                deleteOldImage()
                updateFirebase(downloadUrl)
            }
        } else if (isEditing && !imageUpdated) {
            Log.d("!!!", "editing och ingen ny image")
            val oldImage = knitProject?.imageUrl.toString()
            updateFirebase(oldImage)
        } else {
            Log.d("!!!", "ny projekt")
            uploadImage { downloadUrl ->
                Log.d("!!!", "ny bild: $downloadUrl")
                saveKnitProjectToFirebase(downloadUrl)
            }
        }
    }

    fun updateFirebase(imageUrl: String) {
        val needleSizes = createNeedleList()
        val yarns = createYarnList()

        if (knitProject != null) {
            val knitProject1 = KnitProject(
                documentId = knitProject?.documentId,
                userUid = knitProject?.userUid.toString(),
                ownerName = knitProject?.ownerName.toString(),
                imageUrl = imageUrl,
                projectName = projectName,
                patternName = patternName,
                needleSizes = needleSizes,
                stitches = stitchesAmount,
                rows = rowsAmount,
                yarns = yarns,
                projectNotes = projectNotes,
                createdDate = knitProject!!.createdDate
            )

            if (knitProject!!.documentId != null) {
                val documentId = knitProject!!.documentId as String
                val docRef = db.collection("knitProjects")
                    .document(documentId)

                docRef.set(knitProject1)
                    .addOnSuccessListener {
                        Log.d("!!!", "DocumentSnapShot successfully written")
                        knitProjectViewModel.setSelectedKnitProject(knitProject1)
                    }
                    .addOnFailureListener { error ->
                        Log.d("!!!", "Error writing document", error)
                    }
            }
        }
    }
}