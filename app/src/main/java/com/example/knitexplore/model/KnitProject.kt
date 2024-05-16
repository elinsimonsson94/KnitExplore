package com.example.knitexplore.model

import com.google.firebase.firestore.DocumentId
import java.util.Date


data class KnitProject (
    @DocumentId val documentId : String? = null,
    var ownerName: String = "",
    var userUid: String = "",
    var imageUrl: String = "",
    var projectName: String = "",
    var patternName: String = "",
    var needleSizes: List<Double> = emptyList(),
    var stitches: Int = 0,
    var rows: Int = 0,
    var yarns: List<String> = emptyList(),
    var projectNotes: String = "",
    var createdDate: Date = Date()
)


