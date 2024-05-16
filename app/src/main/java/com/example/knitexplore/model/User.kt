package com.example.knitexplore.model

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId val documentId : String? = null,
    val userUid: String = "",
    val firstName: String = "",
    val lastName: String = ""
)
