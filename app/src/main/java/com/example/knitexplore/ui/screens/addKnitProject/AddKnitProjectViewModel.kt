package com.example.knitexplore.ui.screens.addKnitProject

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.knitexplore.data.KnitProject
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddKnitProjectViewModel : ViewModel() {
    var imageUriState by  mutableStateOf<Uri?>(null)
    var projectName by mutableStateOf("")
    var patternName by mutableStateOf("")
    var needle1 by mutableStateOf(0.0)
    var needle2 by mutableStateOf(0.0)
    var needle3 by mutableStateOf(0.0)
    var needle4 by mutableStateOf(0.0)
    var needle5 by mutableStateOf(0.0)
    private var currentNeedle by mutableStateOf(0) //array always starts on 0
    var needleVisibilityList by mutableStateOf(listOf(true, false, false, false, false))
    var numberOfYarns by mutableIntStateOf(1)
    var stitchesAmount by mutableIntStateOf(0)
    var rowsAmount by mutableIntStateOf(0)
    var projectNotes by mutableStateOf("")

    private val yarns = mutableStateListOf<String>()

    private var storageRef = Firebase.storage.reference
    private var db = Firebase.firestore
    private var auth = Firebase.auth



    fun activateNextNeedle() {
        currentNeedle++
        if (currentNeedle < needleVisibilityList.size) {
            needleVisibilityList = needleVisibilityList.toMutableList().also {
                it[currentNeedle] = true
            }
        }
    }

    fun deActivateNextNeedle() {
        if (currentNeedle <= needleVisibilityList.size) {
            needleVisibilityList = needleVisibilityList.toMutableList().also {
                it[currentNeedle] = false
            }
        }

        when (currentNeedle) {
            1 -> needle2 = 0.0
            2 -> needle3 = 0.0
            3 -> needle4 = 0.0
            4 -> needle5 = 0.0
        }

        currentNeedle--
    }

    private fun createNeedleList () : List<Double> {
        val needleValuesList = mutableStateListOf<Double>()

        listOf(needle1,needle2, needle3, needle4, needle5).forEach { newValue ->
            if (newValue != 0.0) {
                needleValuesList.add(newValue)
            }
        }

        /*needle1 = 0.0
        needle2 = 0.0
        needle3 = 0.0
        needle4 = 0.0
        needle5 = 0.0*/

        return needleValuesList
    }

    fun addYarn (yarn: String) {
        yarns.add(yarn)
    }

    fun increaseNumberYarns() {
        numberOfYarns++
    }



    fun setImageUri (uri : Uri) {
        imageUriState = uri
    }

    private fun saveKnitProjectToFirebase (imageUrl: String) {
        val currentUser = auth.currentUser
        val needleSizeList = createNeedleList()


        currentUser?.let {
            val newKnitProject = KnitProject(
                ownerName = "Elin",
                userUid = currentUser.uid,
                imageUrl = imageUrl,
                projectName = projectName,
                patternName = patternName,
                needleSizes = needleSizeList,
                stitches = stitchesAmount,
                rows = rowsAmount,
                yarns = yarns,
                projectNotes = projectNotes
            )
            Log.d("!!!", "knitProject: $newKnitProject")
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


    fun saveImageToStorage() {

        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val newImageRef = storageRef.child("$fileName.jpg")

        val uploadTask = imageUriState?.let { newImageRef.putFile(it) }

        uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            newImageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                Log.d("!!!", "url som hämtats: $downloadUrl")
                saveKnitProjectToFirebase(downloadUrl)
            } else {
                Log.d("!!!", "failed to download uri ${task.exception}")
            }
        }
    }
}