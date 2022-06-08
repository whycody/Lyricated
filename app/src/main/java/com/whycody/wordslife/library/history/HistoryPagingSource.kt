package com.whycody.wordslife.library.history

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class HistoryPagingSource(private val lastSearchRep: LastSearchRepository,
                          private val languageDao: LanguageDao,
                          private val onlySaved: Boolean): PagingSource<Int, HistoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, HistoryItem>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryItem> {
        val page = params.key ?: 0
        return try {
            val entities = getPagedLastSearches(params, page).map { getHistoryItemFromLastSearch(it) }
            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun getPagedLastSearches(params: LoadParams<Int>, page: Int) =
        if(onlySaved)
            lastSearchRep.getPagedSavedLastSearches(params.loadSize, page * params.loadSize)
        else
            lastSearchRep.getPagedLastSearches(params.loadSize, page * params.loadSize)

    private fun getHistoryItemFromLastSearch(lastSearch: LastSearch) =
        HistoryItem(lastSearch.id, lastSearch.text,
            languageDao.getLanguage(lastSearch.mainLanguageId)!!.drawable,
            languageDao.getLanguage(lastSearch.translationLanguageId)!!.drawable,
            lastSearch.saved)
}