package com.whycody.wordslife.search.lyric.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentMovieBinding
import com.whycody.wordslife.search.lyric.LyricFragment
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {

    private var lyricId = 0
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentMovieBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_movie, container, false)
        if(savedInstanceState == null) addHeader()
        lyricId = arguments?.getInt(LyricFragment.LYRIC_ID, 0)!!
        binding.movieViewModel = movieViewModel
        binding.lifecycleOwner = activity
        observeExtendedLyricItem()
        return binding.root
    }

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity, {
            movieViewModel.findMovie(it)
        })

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getString(R.string.production)))
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(lyricId: Int) =
            MovieFragment().apply {
                arguments = Bundle().apply {
                    putInt(LyricFragment.LYRIC_ID, lyricId)
                }
            }
    }
}