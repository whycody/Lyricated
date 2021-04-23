package com.whycody.wordslife.search.lyric.vocabulary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import com.whycody.wordslife.search.lyric.vocabulary.recycler.VocabularyAdapter
import kotlinx.android.synthetic.main.fragment_vocabulary.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class VocabularyFragment : Fragment(), VocabularyInteractor {

    private val searchViewModel: SearchViewModel by sharedViewModel()
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val vocabularyViewModel: VocabularyViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vocabulary, container, false)
        if(savedInstanceState == null) addHeader()
        observeExtendedLyricItem()
        setupRecycler(view.vocabularyRecycler)
        return view
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getString(R.string.vocabulary)))
        fragmentTransaction.commit()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        with(VocabularyAdapter(this)) {
            recyclerView.adapter = this
            recyclerView.itemAnimator = null
            recyclerView.layoutManager = getFlexboxLayoutManager()
            observeVocabularyItems(this)
        }
    }

    private fun getFlexboxLayoutManager(): FlexboxLayoutManager {
        val flexboxManager = FlexboxLayoutManager(context)
        flexboxManager.flexDirection = FlexDirection.ROW
        flexboxManager.justifyContent = JustifyContent.FLEX_START
        return flexboxManager
    }

    private fun observeVocabularyItems(adapter: VocabularyAdapter) = vocabularyViewModel
        .getVocabularyItems().observe(activity as MainActivity, {
            adapter.submitList(it)
            setViewVisibility(!it.isNullOrEmpty())
        })

    private fun setViewVisibility(vocabularyAvailable: Boolean) {
        if(!vocabularyAvailable) view?.visibility = View.GONE
        else view?.visibility = View.VISIBLE
    }

    private fun observeExtendedLyricItem() = lyricViewModel.getCurrentExtendedLyricItem()
        .observe(activity as MainActivity, { vocabularyViewModel.findVocabulary(it)} )

    override fun wordClicked(word: String) = searchViewModel.searchWord(word)

}