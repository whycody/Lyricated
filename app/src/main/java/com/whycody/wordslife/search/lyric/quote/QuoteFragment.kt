package com.whycody.wordslife.search.lyric.quote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentQuoteBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

class QuoteFragment : Fragment() {

    private val lyricViewModel: LyricViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentQuoteBinding.inflate(inflater)
        if(savedInstanceState == null) addHeader()
        binding.lyricViewModel = lyricViewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
                HeaderFragment.newInstance(getString(R.string.lyric)))
        fragmentTransaction.commit()
    }
}