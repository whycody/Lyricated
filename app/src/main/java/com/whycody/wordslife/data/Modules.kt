package com.whycody.wordslife.data

import android.app.Application
import androidx.room.Room
import com.whycody.wordslife.choose.language.ChooseLanguageViewModel
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.last.searches.LastSearchDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.home.HomeViewModel
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

    fun provideLastSearchDao(database: MyDatabase): LastSearchDao {
        return  database.lastSearchDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideLastSearchDao(get()) }
}

val repositoryModule = module {
    single { LastSearchRepository(get(), get())}
    single { ChooseLanguageRepository(get()) }
}

val languageModule = module {
    single<LanguageDao> { LanguageDaoImpl(get()) }
}

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ChooseLanguageViewModel(get()) }
}
