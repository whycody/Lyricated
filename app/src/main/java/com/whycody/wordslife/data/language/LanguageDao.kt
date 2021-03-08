package com.whycody.wordslife.data.language

import com.whycody.wordslife.data.Language

interface LanguageDao {

    fun getLanguage(id: String): Language?

    fun getAllLanguages(): List<Language>

    fun switchCurrentLanguages()

    fun setCurrentLanguages(mainLanguageId: String, translationLanguageId: String)

    fun getCurrentMainLanguage(): Language

    fun getCurrentTranslationLanguage(): Language

    fun setCurrentMainLanguage(id: String)

    fun setCurrentTranslationLanguage(id: String)
}