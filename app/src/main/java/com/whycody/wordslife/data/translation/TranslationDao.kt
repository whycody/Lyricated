package com.whycody.wordslife.data.translation

import com.beust.klaxon.Klaxon
import com.squareup.okhttp.*
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.TranslationQuery
import com.whycody.wordslife.data.language.LanguageDao

class TranslationDao(private val languageDao: LanguageDao) {

    var client = OkHttpClient()

    fun tryTranslate(phrase: String): List<Translation>? {
        return try {
            val formattedPhrase = phrase.trim().replace("\"`~\\|<>{}[]°•○●□■♤♡◇♧☆▪︎¤《》¡¿", "")
            val multipleTranslations = getMultipleTranslations(formattedPhrase)
            if(multipleTranslations.isNullOrEmpty()) translate(formattedPhrase)
            else multipleTranslations
        } catch(_: Exception) { null }
    }

    @Throws(Exception::class)
    private fun translate(phrase: String): List<Translation> {
        getMultipleTranslations(phrase)
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, "[{\"Text\": \"$phrase\"}]")
        val request = Request.Builder().url(getHttpUrl()).post(body)
            .addHeader("Ocp-Apim-Subscription-Key", TranslationConfig.API_KEY)
            .addHeader("Ocp-Apim-Subscription-Region", TranslationConfig.API_LOCATION)
            .addHeader("Content-type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        val translation = response.body().string()
        val changedTranslation = translation.substring(18, translation.length - 3)
        val translatedPhrase = getTranslatedPhrase(changedTranslation)
        translatedPhrase?.translationLangId = languageDao.getCurrentTranslationLanguage().id
        return listOf(translatedPhrase!!)
    }

    @Throws(Exception::class)
    private fun getMultipleTranslations(phrase: String): List<Translation> {
        val formBody: RequestBody = FormEncodingBuilder().add("request", getRequestBody(phrase)).build()
        val request = Request.Builder().url("https://translate.lyricated.com/api/translate").post(formBody).build()
        val response = client.newCall(request).execute()
        val translationQuery = Klaxon().parse<TranslationQuery>(response.body().string())
        return if(translationQuery?.status?.toInt() == 0) translationQuery.result!!.map { getTranslation(it) }
        else emptyList()
    }

    private fun getRequestBody(phrase: String) =
            "{\"from\": \"${languageDao.getCurrentMainLanguage().id}\"," +
                    " \"to\": \"${languageDao.getCurrentTranslationLanguage().id}\", \"phrase\": \"$phrase\"}"

    private fun getTranslation(phrase: String) =
            Translation(phrase, translationLangId = languageDao.getCurrentMainLanguage().id)

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