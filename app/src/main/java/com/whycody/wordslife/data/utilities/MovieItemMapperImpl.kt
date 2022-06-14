package com.whycody.wordslife.data.utilities

import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class MovieItemMapperImpl(private val movieRepository: MovieRepository,
                          private val languageDao: LanguageDao,
                          private val searchConfigurationDao: SearchConfigurationDao): MovieItemMapper {

    override fun getMovieListItemFromMovie(movie: Movie): MovieListItem {
        val currentSearchConf = searchConfigurationDao.getSearchConfiguration()
        return MovieListItem(
            movie.id,
            movie.type,
            movie.netflixid,
            getTitleFromMovieInLang(languageDao.getCurrentMainLanguage().id, movie, true)!!,
            getTitleFromMovieInLang(languageDao.getCurrentTranslationLanguage().id, movie, false),
            getAllTitlesFromMovie(movie),
            currentSearchConf.chosenSource == movie.id)
    }

    private fun getAllTitlesFromMovie(movie: Movie): String {
        var allTitles = with(movie) { "$eng $esp $fr $ger $it $pl $pt" }
        allTitles = allTitles.replace("null", "").lowercase()
        return allTitles
    }

    override fun getMovieItemFromExtendedLyricItem(extendedLyricItem: ExtendedLyricItem): MovieItem {
        val movie = movieRepository.getMovieWithId(extendedLyricItem.movieId)
        val episode = extendedLyricItem.episode
        return MovieItem(
            getTitleFromMovieInLang(extendedLyricItem.languages.mainLangId, movie, true)!!,
            getTitleFromMovieInLang(extendedLyricItem.languages.translationLangId, movie, false),
            getMovieNetflixid(movie, episode),
            movie.type,
            episode,
            extendedLyricItem.time)
    }

    private fun getMovieNetflixid(movie: Movie, episode: Episode?) = movie.netflixid ?: episode?.netflixid

    private fun getTitleFromMovieInLang(langId: String, movie: Movie, main: Boolean): String? {
        val originalTitle = getTitleInLang(movie.lang, movie)
        return if(main) getMainTitle(originalTitle, movie)
        else getTranslationTitle(langId, originalTitle, movie)
    }

    private fun getMainTitle(originalTitle: String?, movie: Movie) =
        originalTitle?: getTitleInLang(LanguageDaoImpl.EN, movie)

    private fun getTranslationTitle(langId: String, originalTitle: String?, movie: Movie) =
        if(langId != movie.lang && (originalTitle != null || langId != LanguageDaoImpl.EN))
            getTitleInLang(langId, movie)
        else null

    private fun getTitleInLang(langId: String, movie: Movie) =
        when(langId) {
            LanguageDaoImpl.PL -> movie.pl
            LanguageDaoImpl.EN -> movie.eng
            LanguageDaoImpl.PT -> movie.pt
            LanguageDaoImpl.DE -> movie.ger
            LanguageDaoImpl.FR -> movie.fr
            LanguageDaoImpl.ES -> movie.esp
            LanguageDaoImpl.IT -> movie.it
            else -> null
        }
}