package com.whycody.wordslife.data

import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import org.koin.dsl.module

val languageModule = module {
    single<LanguageDao> { LanguageDaoImpl(get()) }
}