package com.whycody.lyricated.choose.language

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.IOnBackPressed
import com.whycody.lyricated.R
import com.whycody.lyricated.choose.language.recycler.ChooseLanguageAdapter
import com.whycody.lyricated.data.Language
import com.whycody.lyricated.data.language.LanguageDaoImpl
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.databinding.FragmentChooseLanguageBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseLanguageFragment : Fragment(), IOnBackPressed {

    private val viewModel: ChooseLanguageViewModel by viewModel()
    private val searchConfigurationDao: SearchConfigurationDao by inject()
    private lateinit var binding: FragmentChooseLanguageBinding
    private var mainLanguage = true
    private var initialConfig = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentChooseLanguageBinding.inflate(inflater)
        mainLanguage = arguments?.getBoolean(LanguageDaoImpl.MAIN_LANGUAGE, true)!!
        viewModel.setMainLanguage(mainLanguage)
        initialConfig = viewModel.getCurrentLanguageID() == LanguageDaoImpl.UNSET
        if(!mainLanguage && initialConfig) observeSearchConf()
        binding.mainLanguage = mainLanguage
        binding.initialConfiguration = initialConfig
        setupRecycler(binding.chooseLanguageRecycler)
        hideKeyboard()
        return binding.root
    }

    private fun observeSearchConf() = searchConfigurationDao.getSearchConfigurationLiveData()
        .observe(viewLifecycleOwner) { viewModel.refreshLanguages() }

    override fun onBackPressed() = viewModel.getCurrentLanguageID() != LanguageDaoImpl.UNSET

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        view?.clearFocus()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        val adapter = ChooseLanguageAdapter(viewModel)
        if(!mainLanguage && initialConfig) observeInitialConfigLanguages(adapter)
        else observeLanguagesList(adapter)
        loadLayoutAnimation(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeLanguagesList(adapter: ChooseLanguageAdapter) =
        viewModel.getLanguages().observe(viewLifecycleOwner) {
            if (adapter.currentList.size != 0) activity?.onBackPressed()
            submitLanguages(adapter, it)
        }

    private fun observeInitialConfigLanguages(adapter: ChooseLanguageAdapter) =
        viewModel.getInitialConfigLanguages().observe(viewLifecycleOwner) {
            val lyricLangs = searchConfigurationDao.getSearchConfiguration().lyricLanguages
            val translationLangIsSet = lyricLangs.translationLangId != LanguageDaoImpl.UNSET
            if(initialConfig && translationLangIsSet) activity?.onBackPressed()
            submitLanguages(adapter, it)
        }

    private fun submitLanguages(adapter: ChooseLanguageAdapter, languages: List<Language>) {

        adapter.submitList(languages)
        binding.chooseLanguageRecycler.scheduleLayoutAnimation()
    }

    private fun loadLayoutAnimation(recyclerView: RecyclerView) {
        val layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_fall_down)
        recyclerView.layoutAnimation = layoutAnimationController
    }

    companion object {
        fun newInstance(mainLanguage: Boolean): ChooseLanguageFragment {
            val fragment = ChooseLanguageFragment()
            with(Bundle()) {
                putBoolean(LanguageDaoImpl.MAIN_LANGUAGE, mainLanguage)
                fragment.arguments = this
            }
            return fragment
        }
    }

}