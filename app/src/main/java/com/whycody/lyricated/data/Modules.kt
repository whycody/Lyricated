package com.whycody.lyricated.data

import android.app.Application
import androidx.room.Room
import com.whycody.lyricated.choose.language.ChooseLanguageViewModel
import com.whycody.lyricated.data.api.ApiService
import com.whycody.lyricated.data.api.BASE_URL
import com.whycody.lyricated.data.app.configuration.AppConfigurationDao
import com.whycody.lyricated.data.app.configuration.AppConfigurationDaoImpl
import com.whycody.lyricated.data.filter.FilterDao
import com.whycody.lyricated.data.filter.FilterDaoImpl
import com.whycody.lyricated.search.filter.choose.source.ChooseSourceViewModel
import com.whycody.lyricated.data.language.ChooseLanguageRepository
import com.whycody.lyricated.data.language.LanguageDao
import com.whycody.lyricated.data.language.LanguageDaoImpl
import com.whycody.lyricated.data.last.searches.LastSearchRepository
import com.whycody.lyricated.data.library.LibraryDao
import com.whycody.lyricated.data.library.LibraryDaoImpl
import com.whycody.lyricated.data.movie.MovieRepository
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDaoImpl
import com.whycody.lyricated.data.settings.SettingsDao
import com.whycody.lyricated.data.settings.SettingsDaoImpl
import com.whycody.lyricated.data.sort.SortDao
import com.whycody.lyricated.data.sort.SortDaoImpl
import com.whycody.lyricated.data.studymode.StudyModeDao
import com.whycody.lyricated.data.studymode.StudyModeDaoImpl
import com.whycody.lyricated.data.utilities.*
import com.whycody.lyricated.home.HomeViewModel
import com.whycody.lyricated.library.LibraryViewModel
import com.whycody.lyricated.library.history.HistoryViewModel
import com.whycody.lyricated.library.most.viewed.LibraryHeaderViewModel
import com.whycody.lyricated.library.settings.SettingsViewModel
import com.whycody.lyricated.library.studymode.StudyModeViewModel
import com.whycody.lyricated.library.studymode.settings.StudyModeSettingsViewModel
import com.whycody.lyricated.main.MainViewModel
import com.whycody.lyricated.search.SearchViewModel
import com.whycody.lyricated.search.configuration.ConfigurationViewModel
import com.whycody.lyricated.search.filter.FilterViewModel
import com.whycody.lyricated.search.lyric.LyricViewModel
import com.whycody.lyricated.search.lyric.movie.MovieViewModel
import com.whycody.lyricated.search.lyric.translation.LyricTranslationViewModel
import com.whycody.lyricated.search.lyric.vocabulary.VocabularyViewModel
import com.whycody.lyricated.search.mapper.LyricItemMapper
import com.whycody.lyricated.search.mapper.LyricItemMapperImpl
import com.whycody.lyricated.search.result.SearchResultViewModel
import com.whycody.lyricated.search.result.span.builder.SearchResultSpanBuilder
import com.whycody.lyricated.search.result.span.builder.SearchResultSpanBuilderImpl
import com.whycody.lyricated.search.sort.SortViewModel
import com.whycody.lyricated.search.translation.TranslationViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    fun provideDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(application, MyDatabase::class.java, "MyDatabase")
                .allowMainThreadQueries()
                .build()
    }

    fun provideLastSearchDao(database: MyDatabase) = database.lastSearchDao()

    fun provideMovieDao(database: MyDatabase) = database.movieDao()

    single { provideDatabase(androidApplication()) }
    single { provideLastSearchDao(get()) }
    single { provideMovieDao(get()) }
}

val retrofitModule = module {

    single<Retrofit> {
        return@single Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

private fun getOkHttpClient() = OkHttpClient.Builder().build()

val repositoryModule = module {
    single { LastSearchRepository(get())}
    single { ChooseLanguageRepository(get()) }
    single { MovieRepository(get()) }
}

val languageModule = module {
    single<LanguageDao> { LanguageDaoImpl(get(), get()) }
}

val libraryModule = module {
    single<LibraryDao> { LibraryDaoImpl(get()) }
}

val settingsModule = module {
    single<AppConfigurationDao> { AppConfigurationDaoImpl(get()) }
    single<SettingsDao> { SettingsDaoImpl(get(), get(), get())}
}

val studyModeModule = module {
    single<StudyModeDao> { StudyModeDaoImpl(get(), get()) }
}

val configurationModule = module {
    single<SortDao> { SortDaoImpl(get(), get()) }
    single<FilterDao> { FilterDaoImpl(get(), get(), get()) }
    single<SearchConfigurationDao> { SearchConfigurationDaoImpl(get()) }
}

val utilsModule = module {
    single<SearchResultSpanBuilder> { SearchResultSpanBuilderImpl(get()) }
    single<LyricItemMapper> { LyricItemMapperImpl(get(), get()) }
    single<MovieItemMapper> { MovieItemMapperImpl(get(), get(), get()) }
    single<MoviePlayer> { MoviePlayerImpl(get()) }
    single<TextCopyUtility> { TextCopyUtilityImpl(get()) }
}

val viewModelsModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchResultViewModel(get()) }
    viewModel { LyricViewModel() }
    viewModel { MovieViewModel(get()) }
    viewModel { VocabularyViewModel() }
    viewModel { LyricTranslationViewModel(get()) }
    viewModel { TranslationViewModel(get()) }
    viewModel { SortViewModel(get(), get()) }
    viewModel { FilterViewModel(get(), get()) }
    viewModel { ConfigurationViewModel(get(), get(), get()) }
    viewModel { ChooseSourceViewModel(get(), get(), get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { LibraryHeaderViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get()) }
    viewModel { StudyModeSettingsViewModel(get(), get()) }
    viewModel { StudyModeViewModel(get(), get(), get(), get()) }
}
