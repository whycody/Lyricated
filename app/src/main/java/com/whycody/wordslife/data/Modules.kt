package com.whycody.wordslife.data

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.ChooseLanguageViewModel
import com.whycody.wordslife.data.filter.FilterDao
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.filter.choose.source.ChooseSourceViewModel
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.library.LibraryDao
import com.whycody.wordslife.data.library.LibraryDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilder
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilderImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDaoImpl
import com.whycody.wordslife.data.sort.SortDao
import com.whycody.wordslife.data.sort.SortDaoImpl
import com.whycody.wordslife.data.translation.TranslationDao
import com.whycody.wordslife.home.HomeViewModel
import com.whycody.wordslife.library.LibraryViewModel
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.configuration.ConfigurationViewModel
import com.whycody.wordslife.search.filter.FilterViewModel
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.movie.MovieViewModel
import com.whycody.wordslife.search.lyric.translation.LyricTranslationViewModel
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyViewModel
import com.whycody.wordslife.search.result.SearchResultViewModel
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilder
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilderImpl
import com.whycody.wordslife.search.sort.SortViewModel
import com.whycody.wordslife.search.translation.TranslationViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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
    single<LanguageDao> { LanguageDaoImpl(get(), get()) }
}

val libraryModule = module {
    single<LibraryDao> { LibraryDaoImpl(get()) }
}

val queryModule = module {
    single<LyricsQueryBuilder> { LyricsQueryBuilderImpl(get()) }
    single { TranslationDao(get()) }
}

val configurationModule = module {
    single<SortDao> { SortDaoImpl(get(), get()) }
    single<FilterDao> { FilterDaoImpl(get(), get(), get()) }
    single<SearchConfigurationDao> { SearchConfigurationDaoImpl(get()) }
}

val utilsModule = module {
    single<SearchResultSpanBuilder> { SearchResultSpanBuilderImpl(ContextCompat.getColor(androidContext(), R.color.light_yellow)) }
}

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SearchResultViewModel(get(), get()) }
    viewModel { LyricViewModel(get()) }
    viewModel { MovieViewModel(get()) }
    viewModel { VocabularyViewModel() }
    viewModel { LyricTranslationViewModel(get()) }
    viewModel { TranslationViewModel(get()) }
    viewModel { SortViewModel(get(), get()) }
    viewModel { FilterViewModel(get(), get()) }
    viewModel { ConfigurationViewModel(get(), get(), get()) }
    viewModel { ChooseSourceViewModel(get(), get()) }
    viewModel { LibraryViewModel(get()) }
}
