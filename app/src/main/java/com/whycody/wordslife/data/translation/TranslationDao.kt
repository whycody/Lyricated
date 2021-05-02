package com.whycody.wordslife.data.translation

import com.beust.klaxon.Klaxon
import com.squareup.okhttp.*
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.language.LanguageDao
import java.io.IOException
import java.lang.Exception

class TranslationDao(private val languageDao: LanguageDao) {

    var client = OkHttpClient()

    fun tryTranslate(phrase: String): List<Translation>? {
        return try { translate(phrase) }
        catch(_: Exception) { null }
    }

    @Throws(IOException::class)
    private fun translate(phrase: String): List<Translation> {
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, "[{\"Text\": \"$phrase\"}]")
        val request = Request.Builder().url(getHttpUrl()).post(body)
            .addHeader("Ocp-Apim-Subscription-Key", TranslationConfig.API_KEY)
            .addHeader("Ocp-Apim-Subscription-Region", TranslationConfig.API_LOCATION)
            .addHeader("Content-type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        val translation = response.body().string()
        val changedTranslation = translation.substring(18, translation.length-3)
        val translatedPhrase = getTranslatedPhrase(changedTranslation)
        translatedPhrase?.translationLangId = languageDao.getCurrentTranslationLanguage().id
        return listOf(translatedPhrase!!)
    }

    private fun getTranslatedPhrase(translationJson: String) = Klaxon().parse<Translation>(translationJson)

    private fun getHttpUrl() =
        HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/translate")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", languageDao.getCurrentMainLanguage().id)
            .addQueryParameter("to", languageDao.getCurrentTranslationLanguage().id)
            .build()

}