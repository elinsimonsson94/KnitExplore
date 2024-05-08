package com.example.knitexplore.ui.shared.viewModels

import com.example.knitexplore.data.KnitProject

object KnitProjectViewModel {
    private var knitProject : KnitProject? = null

    fun getInstance() : KnitProject? {
        if (knitProject == null) {
            return null
        }
        return knitProject
    }

    fun setSelectedKnitProject (selectedKnitProject: KnitProject) {
        knitProject = selectedKnitProject
    }
}