package com.example.knitexplore.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.knitexplore.data.KnitProject
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class HomeScreenViewModel : ViewModel() {


    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _knitProjects = MutableLiveData<List<KnitProject>>()
    val knitProjects: LiveData<List<KnitProject>> = _knitProjects


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
                            Log.d("!!!", "knitProject: $knitProject")
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
}
