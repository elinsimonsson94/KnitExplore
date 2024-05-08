package com.example.knitexplore.ui.screens.userPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class UserViewModel : ViewModel() {
    private val knitProjectViewModel = KnitProjectViewModel
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _userKnitProjects = MutableLiveData<List<KnitProject>>()
    val userKnitProjects: LiveData<List<KnitProject>> = _userKnitProjects

    fun fetchKnitProjects() {
        val currentUser = auth.currentUser
        currentUser?.uid.let { uid ->
            db.collection("knitProjects")
                .whereEqualTo("userUid", uid)
                .addSnapshotListener { snapshot, error ->
                    val knitProjects = ArrayList<KnitProject>()
                    if (error != null) {
                        Log.w("!!!", "Listen failed, ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        for (document in snapshot.documents) {
                            Log.d("!!!", "fetchKnit addSnapshot körs")
                            try {
                                val knitProject = document.toObject<KnitProject>()
                                if (knitProject != null) {
                                    knitProjects.add(knitProject)
                                }
                            } catch (e: Exception) {
                                Log.w("!!!", "Error converting snapshot to obejcts ${e.message}")
                            }
                        }
                        _userKnitProjects.value = knitProjects
                    }
                }
        }
    }
    fun setSelectedKnitProject (selectedKnitProject: KnitProject) {
        knitProjectViewModel.setSelectedKnitProject(selectedKnitProject)
    }

    fun resetNavigation(navController: NavHostController) {
        navController.popBackStack(navController.graph.startDestinationId, false)
    }
}