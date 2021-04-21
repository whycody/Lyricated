package com.whycody.wordslife.search.lyric

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.data.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LyricViewModel(private val lyricsRepository: LyricsRepository,
                     private val movieRepository: MovieRepository,
                     languageDao: LanguageDao): ViewModel() {

    private val currentExtendedLyricItem = MutableLiveData<ExtendedLyricItem>()
    private val lyricIdFlow = MutableStateFlow(0)
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages(
            languageDao.getCurrentMainLanguage().id, languageDao.getCurrentTranslationLanguage().id))

    suspend fun collectExtendedLyricItem() =
            flowExtendedLyricItem().collect { currentExtendedLyricItem.postValue(it) }

    private fun flowExtendedLyricItem(): Flow<ExtendedLyricItem> =
            lyricIdFlow.combine(lyricLanguagesFlow) { id, languages ->
        val lyric = lyricsRepository.getLyricWithId(id)
        getExtendedLyricItemFromLyric(lyric, languages)
    }

    private fun getExtendedLyricItemFromLyric(lyric: Lyric, languages: LyricLanguages) =
            ExtendedLyricItem(lyric.lyricId, lyric.time,
                    getSentenceFromLyricInLang(languages.mainLanguageId, lyric),
                    getSentenceFromLyricInLang(languages.translationLanguageId, lyric),
                    getMovieItemFromLyric(lyric, languages))

    private fun getMovieItemFromLyric(lyric: Lyric, languages: LyricLanguages): MovieItem {
        val movie = movieRepository.getMovieWithId(lyric.movieId)
        return MovieItem(
                getTitleFromMovieInLang(languages.mainLanguageId, movie, true)!!,
                getTitleFromMovieInLang(languages.translationLanguageId, movie, false),
                movie.type,
                getEpisodeItemFromMovie(lyric))
    }

    private fun getEpisodeItemFromMovie(lyric: Lyric): EpisodeItem? {
        val episode = movieRepository.getEpisodeWithLyricId(lyric.lyricId)
        return if(episode == null) null
        else EpisodeItem(episode.season, episode.episode)
    }

    private fun getTitleFromMovieInLang(langId: String, movie: Movie, main: Boolean): String? {
        val originalTitle = getTitleInLang(movie.lang, movie)
        return if(main) getMainTitle(originalTitle, movie)
        else getTranslationTitle(langId, originalTitle, movie)
    }

    private fun getMainTitle(originalTitle: String?, movie: Movie) =
            originalTitle?: getTitleInLang(LanguageDaoImpl.ENG, movie)

    private fun getTranslationTitle(langId: String, originalTitle: String?, movie: Movie) =
            if(langId != movie.lang && (originalTitle != null || langId != LanguageDaoImpl.ENG))
                getTitleInLang(langId, movie)
            else null

    private fun getTitleInLang(langId: String, movie: Movie) =
            when(langId) {
                LanguageDaoImpl.PL -> movie.pl
                LanguageDaoImpl.ENG -> movie.eng
                LanguageDaoImpl.PT -> movie.pt
                LanguageDaoImpl.GER -> movie.ger
                LanguageDaoImpl.FR -> movie.fr
                LanguageDaoImpl.ESP -> movie.esp
                LanguageDaoImpl.IT -> movie.it
                else -> null
            }

    private fun getSentenceFromLyricInLang(langId: String, lyric: Lyric) =
            when(langId) {
                LanguageDaoImpl.PL -> lyric.pl
                LanguageDaoImpl.ENG -> lyric.eng
                LanguageDaoImpl.PT -> lyric.pt
                LanguageDaoImpl.GER -> lyric.ger
                LanguageDaoImpl.FR -> lyric.fr
                LanguageDaoImpl.ESP -> lyric.esp
                LanguageDaoImpl.IT -> lyric.it
                else -> null
            }

    fun searchLyricItem(lyricId: Int) = lyricIdFlow.tryEmit(lyricId)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun getCurrentExtendedLyricItem() = currentExtendedLyricItem
}