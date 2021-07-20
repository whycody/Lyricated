package com.whycody.wordslife

import android.app.Application
import com.whycody.wordslife.data.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(dataModule, repositoryModule, languageModule, queryModule,
                configurationModule, utilsModule, viewModelsModule)
        }
    }
}