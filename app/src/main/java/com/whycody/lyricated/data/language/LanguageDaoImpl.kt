package com.whycody.lyricated.data.language

import android.content.Context
import androidx.core.content.ContextCompat
import com.whycody.lyricated.R
import com.whycody.lyricated.data.Language
import com.whycody.lyricated.data.LyricLanguages
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao

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
                    ContextCompat.getDrawable(context,R.drawable.ic_italy)!!)
    )

    override fun setCurrentLanguages(mainLangId: String, translLangId: String) {
        if(!currentLanguagesAreDifferent(mainLangId, translLangId)) return
        setCurrentMainLanguage(mainLangId)
        setCurrentTranslationLanguage(translLangId)
    }

    private fun currentLanguagesAreDifferent(mainLangId: String, translationLangId: String) =
            mainLangId != getCurrentMainLanguage().id ||
                    translationLangId != getCurrentTranslationLanguage().id

    override fun getCurrentMainLanguage() =
        getLanguage(searchConfigurationDao.getSearchConfiguration().lyricLanguages.mainLangId)
            ?: Language(UNSET, "", ContextCompat.getDrawable(context,R.drawable.ic_italy)!!)

    override fun getCurrentTranslationLanguage() =
        getLanguage(searchConfigurationDao.getSearchConfiguration().lyricLanguages.translationLangId)
            ?: Language(UNSET, "", ContextCompat.getDrawable(context,R.drawable.ic_italy)!!)

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
        const val UNSET = "unset"

        const val MAIN_LANGUAGE = "main_language"
    }
}