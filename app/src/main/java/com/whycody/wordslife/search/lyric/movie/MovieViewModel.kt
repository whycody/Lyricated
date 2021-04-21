package com.whycody.wordslife.search.lyric.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.movie.MovieRepository

class MovieViewModel(private val movieRepository: MovieRepository): ViewModel() {

    private val movieItem = MutableLiveData<MovieItem>()

    fun getMovieItem() = movieItem

    fun findMovie(extendedLyricItem: ExtendedLyricItem) =
        movieItem.postValue(getMovieItemFromLyric(extendedLyricItem))

    private fun getMovieItemFromLyric(extendedLyricItem: ExtendedLyricItem): MovieItem {
        val movie = movieRepository.getMovieWithId(extendedLyricItem.movieId)
        return MovieItem(
            getTitleFromMovieInLang(extendedLyricItem.languages.mainLanguageId, movie, true)!!,
            getTitleFromMovieInLang(extendedLyricItem.languages.translationLanguageId, movie, false),
            movie.type,
            getEpisodeItemFromMovie(extendedLyricItem),
            extendedLyricItem.time)
    }

    private fun getEpisodeItemFromMovie(extendedLyricItem: ExtendedLyricItem): EpisodeItem? {
        val episode = movieRepository.getEpisodeWithLyricId(extendedLyricItem.lyricId)
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

}