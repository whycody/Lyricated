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
import com.whycody.wordslife.databinding.FragmentSearchFieldBinding
import com.whycody.wordslife.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFieldFragment : Fragment(), TextWatcher {

    private val searchViewModel: SearchViewModel by sharedViewModel()
    private lateinit var binding: FragmentSearchFieldBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSearchFieldBinding.inflate(inflater)
        binding.lifecycleOwner = activity
        binding.viewModel = searchViewModel
        binding.searchWordInput.addTextChangedListener(this)
        binding.clearBtn.setOnClickListener{ binding.searchWordInput.setText("") }
        setupSearchWordInputAction()
        return binding.root
    }

    private fun setupSearchWordInputAction() =
        binding.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
            val searchWord = binding.searchWordInput.text.toString()
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
        binding.root.clearFocus()
    }

    private fun wordIsCorrect(word: String) =
        word.trim().replace(Regex("[*.?]"), "").isNotEmpty()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        searchViewModel.setCurrentInputText(s.toString())
        setClearBtnVisibility(s)
    }

    private fun setClearBtnVisibility(s: CharSequence?) {
        binding.clearBtn.visibility =
            if(s.isNullOrBlank()) View.INVISIBLE
            else View.VISIBLE
    }

    override fun afterTextChanged(s: Editable?) { }

}