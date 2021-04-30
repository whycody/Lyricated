package com.whycody.wordslife.data.language

import android.content.Context
import android.content.SharedPreferences
import com.whycody.wordslife.R
import com.whycody.wordslife.data.Language

class LanguageDaoImpl(private val context: Context): LanguageDao {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val prefsEditor = sharedPrefs.edit()

    override fun getLanguage(id: String) =
            getAllLanguages().find { language -> language.id == id }

    override fun getAllLanguages() = listOf(
                Language(EN, context.getString(R.string.english),
                        context.getDrawable(R.drawable.ic_unitedkingdom)!!),
                Language(PL, context.getString(R.string.polish),
                        context.getDrawable(R.drawable.ic_poland)!!),
                Language(DE, context.getString(R.string.german),
                        context.getDrawable(R.drawable.ic_germany)!!),
                Language(ES, context.getString(R.string.spanish),
                        context.getDrawable(R.drawable.ic_spain)!!),
                Language(FR, context.getString(R.string.french),
                        context.getDrawable(R.drawable.ic_france)!!),
                Language(PT, context.getString(R.string.portugal),
                        context.getDrawable(R.drawable.ic_portugal)!!),
                Language(IT, context.getString(R.string.italian),
                        context.getDrawable(R.drawable.ic_italy)!!))

    override fun setCurrentLanguages(mainLangId: String, translationLangId: String) {
        if(!currentLanguagesAreDifferent(mainLangId, translationLangId)) return
        setCurrentMainLanguage(mainLangId)
        setCurrentTranslationLanguage(translationLangId)
    }

    private fun currentLanguagesAreDifferent(mainLangId: String, translationLangId: String) =
            mainLangId != getCurrentMainLanguage().id ||
                    translationLangId != getCurrentTranslationLanguage().id

    override fun getCurrentMainLanguage() =
            getLanguage(sharedPrefs.getString(MAIN_LANGUAGE, DEFAULT_MAIN_LANGUAGE)!!)!!

    override fun getCurrentTranslationLanguage() =
            getLanguage(sharedPrefs.getString(TRANSLATION_LANGUAGE, DEFAULT_TRANSLATION_LANGUAGE)!!)!!

    override fun setCurrentMainLanguage(id: String) {
        if(getCurrentTranslationLanguage().id != id) saveCurrentMainLanguage(id)
        else switchCurrentLanguages()
    }

    private fun saveCurrentMainLanguage(id: String) {
        prefsEditor.putString(MAIN_LANGUAGE, id)
        prefsEditor.commit()
    }

    override fun setCurrentTranslationLanguage(id: String) {
        if(getCurrentMainLanguage().id != id) saveCurrentTranslationLanguage(id)
        else switchCurrentLanguages()
    }

    private fun saveCurrentTranslationLanguage(id: String) {
        prefsEditor.putString(TRANSLATION_LANGUAGE, id)
        prefsEditor.commit()
    }

    override fun switchCurrentLanguages() {
        prefsEditor.putString(MAIN_LANGUAGE, getCurrentTranslationLanguage().id)
        prefsEditor.putString(TRANSLATION_LANGUAGE, getCurrentMainLanguage().id)
        prefsEditor.commit()
    }

    companion object {
        const val EN = "en"
        const val PL = "pl"
        const val DE = "de"
        const val ES = "es"
        const val FR = "fr"
        const val PT = "pt"
        const val IT = "it"

        const val DEFAULT_MAIN_LANGUAGE = EN
        const val DEFAULT_TRANSLATION_LANGUAGE = PL
        const val MAIN_LANGUAGE = "main_language"
        const val TRANSLATION_LANGUAGE = "translation_language"
    }
}