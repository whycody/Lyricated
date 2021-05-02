package com.whycody.wordslife.data

import android.app.Application
import androidx.room.Room
import com.whycody.wordslife.choose.language.ChooseLanguageViewModel
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilder
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilderImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.translation.TranslationDao
import com.whycody.wordslife.home.HomeViewModel
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.movie.MovieViewModel
import com.whycody.wordslife.search.lyric.translation.LyricTranslationViewModel
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyViewModel
import com.whycody.wordslife.search.result.SearchResultViewModel
import com.whycody.wordslife.search.translation.TranslationViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    fun provideDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(application, MyDatabase::class.java, "MyDatabase")
                .allowMainThreadQueries()
                .createFromAsset("wordslife.db")
                .build()
    }

    fun provideLastSearchDao(database: MyDatabase) = database.lastSearchDao()

    fun provideLyricsDao(database: MyDatabase) = database.lyricsDao()

    fun provideMovieDao(database: MyDatabase) = database.movieDao()

    fun provideEpisodeDao(database: MyDatabase) = database.episodeDao()

    single { provideDatabase(androidApplication()) }
    single { provideLastSearchDao(get()) }
    single { provideLyricsDao(get()) }
    single { provideMovieDao(get()) }
    single { provideEpisodeDao(get()) }
}

val repositoryModule = module {
    single { LastSearchRepository(get())}
    single { ChooseLanguageRepository(get()) }
    single { LyricsRepository(get(), get()) }
    single { MovieRepository(get(), get()) }
}

val languageModule = module {
    single<LanguageDao> { LanguageDaoImpl(get()) }
}

val queryModule = module {
    single<LyricsQueryBuilder> { LyricsQueryBuilderImpl() }
    single { TranslationDao(get()) }
}

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SearchResultViewModel(get()) }
    viewModel { LyricViewModel(get()) }
    viewModel { MovieViewModel(get()) }
    viewModel { VocabularyViewModel() }
    viewModel { LyricTranslationViewModel(get()) }
    viewModel { TranslationViewModel(get()) }
}
