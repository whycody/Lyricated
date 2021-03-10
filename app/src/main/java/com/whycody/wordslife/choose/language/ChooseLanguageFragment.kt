package com.whycody.wordslife.choose.language

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.recycler.ChooseLanguageAdapter
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentChooseLanguageBinding
import kotlinx.android.synthetic.main.fragment_choose_language.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseLanguageFragment : Fragment() {

    private lateinit var layoutView: View
    private val viewModel: ChooseLanguageViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentChooseLanguageBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_choose_language, container, false)
        val mainLanguage = arguments?.getBoolean(LanguageDaoImpl.MAIN_LANGUAGE, true)!!
        binding.mainLanguage = mainLanguage
        viewModel.setMainLanguage(mainLanguage)
        layoutView = binding.root
        setupRecycler()
        hideKeyboard()
        return layoutView
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
    }

    private fun setupRecycler() {
        val adapter = ChooseLanguageAdapter(viewModel)
        observeLanguagesList(adapter)
        loadLayoutAnimation(layoutView.chooseLanguageRecycler)
        layoutView.chooseLanguageRecycler.layoutManager = LinearLayoutManager(context)
        layoutView.chooseLanguageRecycler.adapter = adapter
    }

    private fun observeLanguagesList(adapter: ChooseLanguageAdapter) {
        viewModel.getLanguages().observe(activity as MainActivity, {
            if(adapter.currentList.size != 0) activity?.onBackPressed()
            adapter.submitList(it)
            layoutView.chooseLanguageRecycler.scheduleLayoutAnimation()
        })
    }

    private fun loadLayoutAnimation(recyclerView: RecyclerView) {
        val layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_fall_down)
        recyclerView.layoutAnimation = layoutAnimationController
    }

}