package com.whycody.lyricated.choose.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.language.ChooseLanguageRepository

class ChooseLanguageViewModel(private val chooseLanguageRepository: ChooseLanguageRepository):
    ViewModel(), ChooseLanguageInteractor {

    private val languages = MutableLiveData(chooseLanguageRepository.getAllLanguages())
    private val initialConfigLanguages = MutableLiveData(chooseLanguageRepository.getAllLanguages()
        .filter { it.id != chooseLanguageRepository.getCurrentMainLanguage().id })
    private var mainLanguage = true

    override fun onLanguageClick(id: String) {
        if(mainLanguage) chooseLanguageRepository.setCurrentMainLanguage(id)
        else chooseLanguageRepository.setCurrentTranslationLanguage(id)
        languages.postValue(chooseLanguageRepository.getAllLanguages())
        initialConfigLanguages.postValue(chooseLanguageRepository.getAllLanguages()
            .filter { it.id != chooseLanguageRepository.getCurrentMainLanguage().id })
    }

    fun refreshLanguages() {
        languages.postValue(chooseLanguageRepository.getAllLanguages())
        initialConfigLanguages.postValue(chooseLanguageRepository.getAllLanguages()
            .filter { it.id != chooseLanguageRepository.getCurrentMainLanguage().id })
    }

    fun setMainLanguage(mainLanguage: Boolean) {
        this.mainLanguage = mainLanguage
    }

    fun getLanguages() = languages

    fun getInitialConfigLanguages() = initialConfigLanguages

    override fun getCurrentLanguageID() =
        if(mainLanguage) chooseLanguageRepository.getCurrentMainLanguage().id
        else chooseLanguageRepository.getCurrentTranslationLanguage().id
}