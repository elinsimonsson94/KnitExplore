package com.example.knitexplore.ui.shared.viewModels

import com.example.knitexplore.data.KnitProject
import java.util.Date

object KnitProjectViewModel {
    private var knitProject : KnitProject? = null


    fun getInstance() : KnitProject? {
        if (knitProject == null) {
            return null
        }
        return knitProject
    }

    fun updateKnitProject (selectedKnitProject: KnitProject) {
        val currentKnitProject = getInstance()
        knitProject?.let {
            knitProject = it.copy(
                documentId = selectedKnitProject.documentId,
                ownerName = selectedKnitProject.ownerName,
                userUid = selectedKnitProject.userUid,
                imageUrl = selectedKnitProject.imageUrl,
                projectName = selectedKnitProject.projectName,
                patternName = selectedKnitProject.patternName,
                needleSizes = selectedKnitProject.needleSizes,
                stitches = selectedKnitProject.stitches,
                rows = selectedKnitProject.rows,
                yarns = selectedKnitProject.yarns,
                projectNotes = selectedKnitProject.projectNotes,
                createdDate = selectedKnitProject.createdDate
            )
        }
    }
}