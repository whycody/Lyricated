package com.whycody.wordslife.search.lyric.translation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentLyricTranslationBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LyricTranslationFragment : Fragment() {

    private lateinit var typeOfPhrase: String
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val lyricTranslationViewModel: LyricTranslationViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentLyricTranslationBinding.inflate(inflater)
        typeOfPhrase = arguments?.getString(TYPE_OF_PHRASE, MAIN_PHRASE)!!
        if(savedInstanceState == null) addHeader()
        observeTranslationItem(binding)
        observeExtendedLyricItem()
        return binding.root
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getHeaderText()))
        fragmentTransaction.commit()
    }

    private fun getHeaderText() =
        if(typeOfPhrase == MAIN_PHRASE) getString(R.string.lyric)
        else getString(R.string.translation)

    private fun observeTranslationItem(binding: FragmentLyricTranslationBinding) =
        lyricTranslationViewModel.getTranslationItem().observe(activity as MainActivity, {
            binding.translationItem = it
        })

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity, {
            lyricTranslationViewModel.findTranslation(it, typeOfPhrase)
        })

    companion object {
        const val TYPE_OF_PHRASE = "type of phrase"
        const val MAIN_PHRASE = "main phrase"
        const val TRANSLATION_PHRASE = "translation phrase"
        fun newInstance(typeOfPhrase: String): LyricTranslationFragment {
            val fragment = LyricTranslationFragment()
            with(Bundle()) {
                putString(TYPE_OF_PHRASE, typeOfPhrase)
                fragment.arguments = this
            }
            return fragment
        }
    }

}