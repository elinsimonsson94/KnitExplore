package com.example.knitexplore.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.knitexplore.data.KnitProject
import com.example.knitexplore.ui.shared.viewModels.KnitProjectViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {

    private val knitProjectViewModel =KnitProjectViewModel
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _knitProjects = MutableLiveData<List<KnitProject>>()
    val knitProjects: LiveData<List<KnitProject>> = _knitProjects

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchedKnitProject = _knitProjects.asFlow()

    val knitProjectList = searchText
        .combine(_searchedKnitProject) {text, knitProjects ->
            if (text.isBlank()) {
                knitProjects
            }
            knitProjects.filter { knitProject ->
                knitProject.patternName.contains(text)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _knitProjects
        )

    fun onSearchTextChange (text: String) {
        _searchText.value = text
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
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
