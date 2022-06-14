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
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.recycler.ChooseLanguageAdapter
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentChooseLanguageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseLanguageFragment : Fragment(), IOnBackPressed {

    private val viewModel: ChooseLanguageViewModel by viewModel()
    private lateinit var binding: FragmentChooseLanguageBinding
    private var mainLanguage = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentChooseLanguageBinding.inflate(inflater)
        mainLanguage = arguments?.getBoolean(LanguageDaoImpl.MAIN_LANGUAGE, true)!!
        viewModel.setMainLanguage(mainLanguage)
        binding.mainLanguage = mainLanguage
        binding.initialConfiguration = viewModel.getCurrentLanguageID() == LanguageDaoImpl.UNSET
        setupRecycler(binding.chooseLanguageRecycler)
        hideKeyboard()
        return binding.root
    }

    override fun onBackPressed() = viewModel.getCurrentLanguageID() != LanguageDaoImpl.UNSET

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
        viewModel.getLanguages().observe(viewLifecycleOwner) {
            if (adapter.currentList.size != 0) activity?.onBackPressed()
            adapter.submitList(it)
            recyclerView.scheduleLayoutAnimation()
        }
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