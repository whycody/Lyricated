package com.whycody.wordslife.choose.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.language.ChooseLanguageRepository

class ChooseLanguageViewModel(private val chooseLanguageRepository: ChooseLanguageRepository):
    ViewModel(), ChooseLanguageInteractor {

    private val languages = MutableLiveData(chooseLanguageRepository.getAllLanguages())
    private var mainLanguage = true

    override fun onLanguageClick(id: String) {
        if(mainLanguage) chooseLanguageRepository.setCurrentMainLanguage(id)
        else chooseLanguageRepository.setCurrentTranslationLanguage(id)
        languages.postValue(chooseLanguageRepository.getAllLanguages())
    }

    fun setMainLanguage(mainLanguage: Boolean) {
        this.mainLanguage = mainLanguage
    }

    fun getLanguages() = languages

    override fun getCurrentLanguageID() =
        if(mainLanguage) chooseLanguageRepository.getCurrentMainLanguage().id
        else chooseLanguageRepository.getCurrentTranslationLanguage().id
}