package com.whycody.wordslife.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.whycody.wordslife.choose.language.ChooseLanguageViewModel
import com.whycody.wordslife.data.api.ApiService
import com.whycody.wordslife.data.api.BASE_URL
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
import com.whycody.wordslife.library.most.viewed.LibraryHeaderViewModel
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
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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

    fun provideLyricsDao(database: MyDatabase) = database.lyricsDao()

    fun provideMovieDao(database: MyDatabase) = database.movieDao()

    fun provideEpisodeDao(database: MyDatabase) = database.episodeDao()

    single { provideDatabase(androidApplication()) }
    single { provideLastSearchDao(get()) }
    single { provideLyricsDao(get()) }
    single { provideMovieDao(get()) }
    single { provideEpisodeDao(get()) }
}

val retrofitModule = module {

    single<Retrofit> {
        return@single Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(androidContext()))
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

private fun getOkHttpClient(context: Context) =
    OkHttpClient.Builder()
        .cache(getCache(context))
        .addInterceptor { chain ->
            var request = chain.request()
            request =
                if (hasNetwork(context))
                    request.newBuilder().header("Cache-Control",
                        "public, max-age= 5").build()
                else request.newBuilder().header("Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }.build()

private fun getCache(context: Context): Cache {
    val cacheSize = (5 * 1024 * 1024).toLong()
    return Cache(context.cacheDir, cacheSize)
}

private fun hasNetwork(context: Context): Boolean {
    var isConnected = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if(activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
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
    single<SearchResultSpanBuilder> { SearchResultSpanBuilderImpl(get()) }
}

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get()) }
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
    viewModel { LibraryHeaderViewModel(get()) }
}
