package com.whycody.wordslife.data.language

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.Language

class LanguageDaoImpl(private val context: Context): LanguageDao {

    override fun getLanguage(id: String) =
            getAllLanguages().find { language -> language.id == id }

    override fun getAllLanguages() = listOf(
                Language(ENG, context.getString(R.string.english),
                        context.getDrawable(R.drawable.ic_unitedkingdom)!!),
                Language(PL, context.getString(R.string.polish),
                        context.getDrawable(R.drawable.ic_poland)!!),
                Language(GER, context.getString(R.string.german),
                        context.getDrawable(R.drawable.ic_germany)!!),
                Language(ESP, context.getString(R.string.spanish),
                        context.getDrawable(R.drawable.ic_spain)!!),
                Language(FR, context.getString(R.string.french),
                        context.getDrawable(R.drawable.ic_france)!!),
                Language(PT, context.getString(R.string.portugal),
                        context.getDrawable(R.drawable.ic_portugal)!!),
                Language(IT, context.getString(R.string.italian),
                        context.getDrawable(R.drawable.ic_italy)!!))

    companion object {
        const val ENG = "eng"
        const val PL = "pl"
        const val GER = "ger"
        const val ESP = "esp"
        const val FR = "fr"
        const val PT = "pt"
        const val IT = "it"
    }
}