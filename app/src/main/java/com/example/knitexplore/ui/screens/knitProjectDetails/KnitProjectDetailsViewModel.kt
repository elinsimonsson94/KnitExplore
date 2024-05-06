package com.example.knitexplore.ui.screens.knitProjectDetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.data.NavigationItem
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class KnitProjectDetailsViewModel : ViewModel() {
    private var knitProjectViewModel = KnitProjectViewModel
    var knitProjectInstance = KnitProjectViewModel.getInstance()
    private val _selectedKnitProject = MutableLiveData<KnitProject>()
    var selectedKnitProject: LiveData<KnitProject?> = _selectedKnitProject

    private val db = Firebase.firestore

    var auth = Firebase.auth
    val currentUser = auth.currentUser

    private var selectedKnitProjectState = mutableStateOf(KnitProject())

    fun getSelectedKnitProject():State<KnitProject> {
        return selectedKnitProjectState
    }

    fun refresh() {
        selectedKnitProjectState.value = KnitProjectViewModel.getInstance()!!
    }

    private fun setSelectedKnitProject (project: KnitProject) {
        _selectedKnitProject.value = project
    }

    fun listenToDocument() {
        val documentId = knitProjectInstance?.documentId as String

        val docRef = db.collection("knitProjects").document(documentId)

        docRef.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                try {
                    val knitProject = snapshot.toObject(KnitProject::class.java)
                    if (knitProject != null) {
                        setSelectedKnitProject(knitProject)
                    }
                } catch (e: Exception) {
                    Log.d("!!!", "Error converting to KnitProject")
                }
            }
        }
    }
    fun setEditAndNavigate(navController: NavHostController) {
        knitProjectInstance?.let {
            knitProjectViewModel.setSelectedKnitProject(it)
            val isEditing = true
            val route = NavigationItem.AddKnitProject.createRoute(isEditing = isEditing)
            navController.navigate(route)
        }
    }
}