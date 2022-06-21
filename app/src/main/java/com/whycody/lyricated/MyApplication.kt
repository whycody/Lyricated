package com.whycody.lyricated

import android.app.Application
import com.whycody.lyricated.data.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(dataModule, retrofitModule, repositoryModule, languageModule, libraryModule,
                settingsModule, studyModeModule, configurationModule, utilsModule, viewModelsModule)
        }
    }
}