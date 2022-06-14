package com.whycody.wordslife.search.lyric.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.utilities.MoviePlayer
import com.whycody.wordslife.databinding.FragmentMovieBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment(), MovieInteractor {

    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val movieViewModel: MovieViewModel by viewModel()
    private val moviePlayer: MoviePlayer by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMovieBinding.inflate(inflater)
        if(savedInstanceState == null) addHeader()
        binding.interactor = this
        binding.movieViewModel = movieViewModel
        binding.lifecycleOwner = activity
        observeExtendedLyricItem()
        return binding.root
    }

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity) {
            movieViewModel.findMovie(it)
        }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getString(R.string.production)))
        fragmentTransaction.commit()
    }

    override fun playBtnClicked(netflixid: Int, time: String) = moviePlayer.playMovie(netflixid, time)
}