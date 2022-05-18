package com.whycody.wordslife.data

import android.app.Application
import androidx.room.Room
import com.whycody.wordslife.choose.language.ChooseLanguageViewModel
import com.whycody.wordslife.data.api.ApiService
import com.whycody.wordslife.data.api.BASE_URL
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao
import com.whycody.wordslife.data.app.configuration.AppConfigurationDaoImpl
import com.whycody.wordslife.data.filter.FilterDao
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.filter.choose.source.ChooseSourceViewModel
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.library.LibraryDao
import com.whycody.wordslife.data.library.LibraryDaoImpl
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDaoImpl
import com.whycody.wordslife.data.settings.SettingsDao
import com.whycody.wordslife.data.settings.SettingsDaoImpl
import com.whycody.wordslife.data.sort.SortDao
import com.whycody.wordslife.data.sort.SortDaoImpl
import com.whycody.wordslife.home.HomeViewModel
import com.whycody.wordslife.library.LibraryViewModel
import com.whycody.wordslife.library.most.viewed.LibraryHeaderViewModel
import com.whycody.wordslife.library.settings.SettingsViewModel
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.configuration.ConfigurationViewModel
import com.whycody.wordslife.search.filter.FilterViewModel
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.movie.MovieViewModel
import com.whycody.wordslife.search.lyric.translation.LyricTranslationViewModel
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyViewModel
import com.whycody.wordslife.search.mapper.LyricItemMapper
import com.whycody.wordslife.search.mapper.LyricItemMapperImpl
import com.whycody.wordslife.search.result.SearchResultViewModel
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilder
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilderImpl
import com.whycody.wordslife.search.sort.SortViewModel
import com.whycody.wordslife.search.translation.TranslationViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    fun provideDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(application, MyDatabase::class.java, "MyDatabase")
                .allowMainThreadQueries()
                .createFromAsset("wordslife.db")
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

val configurationModule = module {
    single<SortDao> { SortDaoImpl(get(), get()) }
    single<FilterDao> { FilterDaoImpl(get(), get(), get()) }
    single<SearchConfigurationDao> { SearchConfigurationDaoImpl(get()) }
}

val utilsModule = module {
    single<SearchResultSpanBuilder> { SearchResultSpanBuilderImpl(get()) }
    single<LyricItemMapper> { LyricItemMapperImpl(get()) }
}

val viewModelsModule = module {
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
    viewModel { ChooseSourceViewModel(get(), get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { LibraryHeaderViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}
