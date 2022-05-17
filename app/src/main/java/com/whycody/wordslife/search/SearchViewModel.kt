package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.api.ApiService
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.search.mapper.LyricItemMapper
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val lastSearchRepository: LastSearchRepository,
                      private val searchConfigurationDao: SearchConfigurationDao,
                      private val apiService: ApiService,
                      private val lyricItemMapper: LyricItemMapper): ViewModel() {

    private val searchWord = MutableLiveData<String>()
    private val currentInputText = MutableLiveData("")
    private val userAction = MutableLiveData(UserAction())
    private val translations = MutableLiveData<List<Translation>>()
    private val translationsLoading = MutableLiveData(false)
    private val findLyricsResponseReady = MutableLiveData(false)
    private val findLyricsResponse = MutableLiveData<FindLyricsResponse>()

    fun searchWord(word: String) {
        searchWord.value = word
        if(word.isNotEmpty()) insertLastSearch(word)
    }

    fun tryFindLyricsFromApi() {
        viewModelScope.launch {
            try {
                if(!currentInputText.value.isNullOrEmpty()) sendInquiryToApi()
            } catch(e: Exception) {
                tryFindLyricsFromApi()
            }
        }
    }

    private suspend fun sendInquiryToApi() {
        findLyricsResponseReady.value = false
        val body = getFindLyricsBody()
        val queryResponse = apiService.findLyrics(body).body()
        if(queryResponse?.mainLanguageId != searchConfigurationDao.getLyricLanguages().mainLangId
            || queryResponse.searchWord != searchWord.value) return
        findLyricsResponse.value = queryResponse
        findLyricsResponseReady.value = true
    }

    private fun getFindLyricsBody(): FindLyricsBody {
        val currentLanguages = searchConfigurationDao.getLyricLanguages()
        val searchConf = searchConfigurationDao.getSearchConfiguration()
        val body = FindLyricsBody(searchWord.value!!, currentLanguages.mainLangId,
            currentLanguages.translationLangId, searchConf.sortOptionId,
            searchConf.checkedFilters.contains(FilterDaoImpl.WITHOUT_CURSES))
        setFindLyricsBodySource(searchConf, body)
        setFindLyricsBodyMovieId(searchConf, body)
        return body
    }

    private fun setFindLyricsBodySource(searchConf: SearchConfiguration, body: FindLyricsBody) {
        if(searchConf.checkedFilters.contains(FilterDaoImpl.ONLY_MOVIES))
            body.source = FilterDaoImpl.ONLY_MOVIES
        else if(searchConf.checkedFilters.contains(FilterDaoImpl.ONLY_SERIES))
            body.source = FilterDaoImpl.ONLY_SERIES
    }

    private fun setFindLyricsBodyMovieId(searchConf: SearchConfiguration, body: FindLyricsBody) {
        if(searchConf.chosenSource!=null) body.movieId = searchConf.chosenSource
    }

    fun setTranslations(translations: List<Translation>) {
        this.translations.value = translations
    }

    private fun insertLastSearch(word: String) {
        searchConfigurationDao.getLyricLanguages().translationLangId
        val lastSearch = getLastSearch(word)
        val exactLastSearch = getExactLastSearch(lastSearch)
        if(exactLastSearch != null) lastSearchRepository.refreshTime(exactLastSearch.id)
        else lastSearchRepository.insertLastSearch(lastSearch)
    }

    fun setTranslationsLoading(loading: Boolean) {
        translationsLoading.value = loading
    }

    fun setCurrentInputText(currentInputText: String) {
        this.currentInputText.value = currentInputText
    }

    fun getSearchWord() = searchWord

    fun setUserAction(actionType: Int, actionId: Int, lyricItem: LyricItem)
        = userAction.postValue(UserAction(actionType, actionId,
        lyricItemMapper.getExtendedLyricItemFromLyricItem(lyricItem)))

    fun getUserAction() = userAction

    fun resetUserAction() = userAction.postValue(UserAction())

    fun getFindLyricsResponse() = findLyricsResponse

    fun getFindLyricsResponseReady() = findLyricsResponseReady

    private fun getExactLastSearch(lastSearch: LastSearch) =
            lastSearchRepository.getAllLastSearches().find { lastSearchesAreTheSame(it, lastSearch) }

    private fun lastSearchesAreTheSame(lsOne: LastSearch, lsTwo: LastSearch) =
            lsOne.text == lsTwo.text && lsOne.mainLanguageId == lsTwo.mainLanguageId &&
                    lsOne.translationLanguageId == lsTwo.translationLanguageId

    private fun getLastSearch(word: String) = LastSearch(
            mainLanguageId = searchConfigurationDao.getLyricLanguages().mainLangId,
            translationLanguageId = searchConfigurationDao.getLyricLanguages().translationLangId,
            text = word)
}