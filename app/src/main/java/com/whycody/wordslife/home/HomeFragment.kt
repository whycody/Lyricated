package com.whycody.wordslife.home

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.MainNavigation
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentHomeBinding
import com.whycody.wordslife.home.history.HistoryAdapter
import com.whycody.wordslife.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.clearBtn
import kotlinx.android.synthetic.main.fragment_home.view.searchWordInput
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), TextWatcher {

    private lateinit var layoutView: View
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false)
        layoutView = binding.root
        layoutView.searchWordInput.addTextChangedListener(this)
        layoutView.clearBtn.setOnClickListener{ searchWordInput.setText("") }
        observeSearchWord()
        setupSearchWordInput()
        setupRecycler(binding)
        startAnimations()
        return layoutView
    }

    private fun observeSearchWord() = homeViewModel.getSearchWord()
            .observe(activity as MainActivity, {
                if(it != "") searchWord(it)
    })

    private fun setupSearchWordInput() =
            layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
                val searchWord = layoutView.searchWordInput.text.toString()
                if(actionId == EditorInfo.IME_ACTION_SEARCH && wordIsCorrect(searchWord))
                    searchWord(searchWord)
                true
            }

    private fun wordIsCorrect(word: String) =
            word.trim().replace(Regex("[*.?]"), "").isNotEmpty()

    private fun searchWord(word: String) {
        hideKeyboard()
        (activity as MainNavigation).navigateTo(SearchFragment.newInstance(word))
        layoutView.searchWordInput.setText("")
        homeViewModel.resetWord()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        layoutView.clearFocus()
    }

    private fun setupRecycler(binding: FragmentHomeBinding) {
        val historyAdapter = HistoryAdapter(homeViewModel)
        observeHistoryItems(binding, historyAdapter)
        binding.historyDisponible = true
        with(binding.root.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentHomeBinding, historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity, {
            binding.historyDisponible = it.isNotEmpty()
            if (historyAdapter.currentList.isEmpty())
                layoutView.historyRecycler.scheduleLayoutAnimation()
            historyAdapter.submitList(it)
        })

    private fun startAnimations() {
        val context = activity?.applicationContext
        with(layoutView) {
            bannerStarsOne.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_one))
            bannerStarsTwo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_two))
            bannerStarsThree.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_three))
            bannerStarsFour.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_four))
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        setClearBtnVisibility(s)
    }

    private fun setClearBtnVisibility(s: CharSequence?) {
        layoutView.clearBtn.visibility =
                if(s.isNullOrBlank()) View.INVISIBLE
                else View.VISIBLE
    }

    override fun afterTextChanged(s: Editable?) {  }

}