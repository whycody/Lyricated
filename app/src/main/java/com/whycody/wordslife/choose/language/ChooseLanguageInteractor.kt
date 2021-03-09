package com.whycody.wordslife.choose.language

interface ChooseLanguageInteractor {

    fun onLanguageClick(id: String)

    fun getCurrentLanguageID(): String
}