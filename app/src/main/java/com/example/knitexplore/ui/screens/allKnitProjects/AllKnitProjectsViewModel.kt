package com.example.knitexplore.ui.screens.allKnitProjects

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AllKnitProjectsViewModel: ViewModel() {

    private val knitProjectViewModel = KnitProjectViewModel
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _knitProjects = MutableLiveData<List<KnitProject>>()
    val knitProjects: LiveData<List<KnitProject>> = _knitProjects

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    fun onSearchTextChange (text: String) {
        _searchText.value = text
    }

    fun fetchKnitProjects() {
        db.collection("knitProjects")
            .addSnapshotListener { snapshot, error ->
                val knitProjects = ArrayList<KnitProject>()
                if (error != null) {
                    Log.w("!!!", "Listen failed, ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // clear the list
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
                    _knitProjects.value = knitProjects
                }
            }
    }

    fun setSelectedKnitProject (selectedKnitProject: KnitProject) {
        knitProjectViewModel.setSelectedKnitProject(selectedKnitProject)
    }
}