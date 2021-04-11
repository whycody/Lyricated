package com.whycody.wordslife.search.field

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchFieldBinding
import com.whycody.wordslife.search.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_field.*
import kotlinx.android.synthetic.main.fragment_search_field.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFieldFragment : Fragment(), TextWatcher {

    private lateinit var layoutView: View
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchFieldBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search_field, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = searchViewModel
        layoutView = binding.root
        setupSearchWordInputAction()
        layoutView.searchWordInput.addTextChangedListener(this)
        layoutView.clearBtn.setOnClickListener{ searchWordInput.setText("") }
        return layoutView
    }

    private fun setupSearchWordInputAction() =
        layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
            val searchWord = layoutView.searchWordInput.text.toString()
            if(actionId == EditorInfo.IME_ACTION_SEARCH && wordIsCorrect(searchWord))
                searchWord(searchWord)
            true
        }

    private fun searchWord(searchWord: String) {
        searchViewModel.searchWord(searchWord)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) ?: return
        (imm as InputMethodManager).hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        layoutView.clearFocus()
    }

    private fun wordIsCorrect(word: String) =
        word.trim().replace(Regex("[*.?]"), "").isNotEmpty()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        setClearBtnVisibility(s)
    }

    private fun setClearBtnVisibility(s: CharSequence?) {
        layoutView.clearBtn.visibility =
            if(s.isNullOrBlank()) View.INVISIBLE
            else View.VISIBLE
    }

    override fun afterTextChanged(s: Editable?) { }

}