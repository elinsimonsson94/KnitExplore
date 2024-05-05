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

    var numberOfNeedleSizes by mutableIntStateOf(1)
    var numberOfYarns by mutableIntStateOf(1)
    var stitchesAmount by mutableIntStateOf(0)
    var rowsAmount by mutableIntStateOf(0)
    var projectNotes by mutableStateOf("")
    val needleSizes = mutableStateListOf<Double>()
    private val yarns = mutableStateListOf<String>()

    private var storageRef = Firebase.storage.reference
    private var db = Firebase.firestore
    private var auth = Firebase.auth



    fun addNeedleSize(size: String) {
        val sizeAsDouble = size.toDoubleOrNull()
        if (sizeAsDouble != null) {
            Log.d("!!!", "size: $sizeAsDouble")
            needleSizes.add(sizeAsDouble)
        } else {
            Log.d("!!!", "Invalid size: $size")
        }
    }

    fun addYarn (yarn: String) {
        yarns.add(yarn)
    }

    fun increaseNumberYarns() {
        numberOfYarns++
    }

    fun increaseNumberNeedleSizes() {
        numberOfNeedleSizes++
    }

    fun setImageUri (uri : Uri) {
        imageUriState = uri
    }

    private fun convertToDate (timePicked: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return try {
            val today = Date()
            val formattedDateStr = SimpleDateFormat("yyyy-MM-dd").format(today).toString()
            val dateTimeStr = "$formattedDateStr $timePicked"
            dateFormat.parse(dateTimeStr)

        } catch (e: Exception) {
            e.message?.let { Log.d("!!!", it) }
            null
        }
    }

    private fun saveKnitProjectToFirebase (imageUrl: String) {
        val currentUser = auth.currentUser

        currentUser?.let {

            val newKnitProject = KnitProject(
                ownerName = "Elin",
                userUid = currentUser.uid,
                imageUrl = imageUrl,
                projectName = projectName,
                patternName = patternName,
                needleSizes = needleSizes,
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