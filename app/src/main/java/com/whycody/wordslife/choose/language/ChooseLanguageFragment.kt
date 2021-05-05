package com.whycody.wordslife.choose.language

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
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.recycler.ChooseLanguageAdapter
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentChooseLanguageBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseLanguageFragment : Fragment() {

    private val viewModel: ChooseLanguageViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentChooseLanguageBinding.inflate(inflater)
        val mainLanguage = arguments?.getBoolean(LanguageDaoImpl.MAIN_LANGUAGE, true)!!
        binding.mainLanguage = mainLanguage
        viewModel.setMainLanguage(mainLanguage)
        setupRecycler(binding.chooseLanguageRecycler)
        hideKeyboard()
        return binding.root
    }

    fun newInstance(mainLanguage: Boolean): ChooseLanguageFragment {
        val fragment = ChooseLanguageFragment()
        with(Bundle()) {
            putBoolean(LanguageDaoImpl.MAIN_LANGUAGE, mainLanguage)
            fragment.arguments = this
        }
        return fragment
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        view?.clearFocus()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        val adapter = ChooseLanguageAdapter(viewModel)
        observeLanguagesList(adapter, recyclerView)
        loadLayoutAnimation(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeLanguagesList(adapter: ChooseLanguageAdapter, recyclerView: RecyclerView) {
        viewModel.getLanguages().observe(activity as MainActivity, {
            if(adapter.currentList.size != 0) activity?.onBackPressed()
            adapter.submitList(it)
            recyclerView.scheduleLayoutAnimation()
        })
    }

    private fun loadLayoutAnimation(recyclerView: RecyclerView) {
        val layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_fall_down)
        recyclerView.layoutAnimation = layoutAnimationController
    }

}