package com.whycody.wordslife.data.language

import android.content.Context
import androidx.core.content.ContextCompat
import com.whycody.wordslife.R
import com.whycody.wordslife.data.Language
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class LanguageDaoImpl(private val context: Context,
                      private val searchConfigurationDao: SearchConfigurationDao): LanguageDao {

    override fun getLanguage(id: String) =
            getAllLanguages().find { language -> language.id == id }

    override fun getAllLanguages() = listOf(
                Language(EN, context.getString(R.string.english),
                    ContextCompat.getDrawable(context, R.drawable.ic_unitedkingdom)!!),
                Language(PL, context.getString(R.string.polish),
                    ContextCompat.getDrawable(context,R.drawable.ic_poland)!!),
                Language(DE, context.getString(R.string.german),
                    ContextCompat.getDrawable(context,R.drawable.ic_germany)!!),
                Language(ES, context.getString(R.string.spanish),
                    ContextCompat.getDrawable(context,R.drawable.ic_spain)!!),
                Language(FR, context.getString(R.string.french),
                    ContextCompat.getDrawable(context,R.drawable.ic_france)!!),
                Language(PT, context.getString(R.string.portugal),
                    ContextCompat.getDrawable(context,R.drawable.ic_portugal)!!),
                Language(IT, context.getString(R.string.italian),
                    ContextCompat.getDrawable(context,R.drawable.ic_italy)!!))

    override fun setCurrentLanguages(mainLangId: String, translLangId: String) {
        if(!currentLanguagesAreDifferent(mainLangId, translLangId)) return
        setCurrentMainLanguage(mainLangId)
        setCurrentTranslationLanguage(translLangId)
    }

    private fun currentLanguagesAreDifferent(mainLangId: String, translationLangId: String) =
            mainLangId != getCurrentMainLanguage().id ||
                    translationLangId != getCurrentTranslationLanguage().id

    override fun getCurrentMainLanguage() =
        getLanguage(searchConfigurationDao.getSearchConfiguration().lyricLanguages.mainLangId)!!

    override fun getCurrentTranslationLanguage() =
        getLanguage(searchConfigurationDao.getSearchConfiguration().lyricLanguages.translationLangId)!!

    override fun setCurrentMainLanguage(id: String) {
        if(getCurrentTranslationLanguage().id != id) saveCurrentMainLanguage(id)
        else switchCurrentLanguages()
    }

    private fun saveCurrentMainLanguage(id: String) {
        val searchConfiguration = searchConfigurationDao.getSearchConfiguration()
        searchConfiguration.lyricLanguages.mainLangId = id
        searchConfigurationDao.setSearchConfiguration(searchConfiguration)
    }

    override fun setCurrentTranslationLanguage(id: String) {
        if(getCurrentMainLanguage().id != id) saveCurrentTranslationLanguage(id)
        else switchCurrentLanguages()
    }

    private fun saveCurrentTranslationLanguage(id: String) {
        val searchConfiguration = searchConfigurationDao.getSearchConfiguration()
        searchConfiguration.lyricLanguages.translationLangId = id
        searchConfigurationDao.setSearchConfiguration(searchConfiguration)
    }

    override fun switchCurrentLanguages() {
        val searchConfiguration = searchConfigurationDao.getSearchConfiguration()
        val currentLanguages = searchConfiguration.lyricLanguages
        searchConfiguration.lyricLanguages =
            LyricLanguages(currentLanguages.translationLangId, currentLanguages.mainLangId)
        searchConfigurationDao.setSearchConfiguration(searchConfiguration)
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
    }
}