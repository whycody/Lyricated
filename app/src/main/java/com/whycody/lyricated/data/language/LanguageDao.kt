package com.whycody.lyricated.data.language

import com.whycody.lyricated.data.Language

interface LanguageDao {

    fun getLanguage(id: String): Language?

    fun getAllLanguages(): List<Language>

    fun switchCurrentLanguages()

    fun setCurrentLanguages(mainLangId: String, translLangId: String)

    fun getCurrentMainLanguage(): Language

    fun getCurrentTranslationLanguage(): Language

    fun setCurrentMainLanguage(id: String)

    fun setCurrentTranslationLanguage(id: String)
}