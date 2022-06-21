package com.whycody.lyricated.choose.language

interface ChooseLanguageInteractor {

    fun onLanguageClick(id: String)

    fun getCurrentLanguageID(): String
}