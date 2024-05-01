package com.example.knitexplore.ui.screens.addKnitProject

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddKnitProjectViewController : ViewModel() {
    var imageUriState by  mutableStateOf<Uri?>(null)
    var projectName by mutableStateOf("")
    var patternName by mutableStateOf("")
    var needleSize by mutableStateOf<Double?>(null)
    var numberOfNeedleSizes by mutableIntStateOf(1)

    private var storageRef = Firebase.storage.reference

    fun addNeedleSize() {
        numberOfNeedleSizes++
    }

    fun setImageUri (uri : Uri) {
        imageUriState = uri
    }

    fun trySave () {
        Log.d("!!!", "projectName: $projectName patternName: $patternName  needleSize: $needleSize")
    }

    fun updateNeedleSize (number: Double) {
        needleSize = number
    }
    fun updateProjectText (text: String) {
        projectName = text
    }

    fun updatePatternName (text: String) {
        patternName = text
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
                /*saveUserImageToFirestore(downloadUrl, navController)*/
            } else {
                Log.d("!!!", "failed to download uri ${task.exception}")
            }
        }
    }
}