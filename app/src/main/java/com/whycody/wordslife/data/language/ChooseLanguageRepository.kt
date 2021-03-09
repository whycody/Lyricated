package com.whycody.wordslife.data.language

class ChooseLanguageRepository(private val languageDao: LanguageDao) {

    fun getAllLanguages() = languageDao.getAllLanguages()

    fun getCurrentMainLanguage() = languageDao.getCurrentMainLanguage()

    fun getCurrentTranslationLanguage() = languageDao.getCurrentTranslationLanguage()

    fun setCurrentMainLanguage(id: String) = languageDao.setCurrentMainLanguage(id)

    fun setCurrentTranslationLanguage(id: String) = languageDao.setCurrentTranslationLanguage(id)
}