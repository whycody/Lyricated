package com.whycody.wordslife.search.lyric.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentMovieBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MovieFragment : Fragment() {

    private val lyricViewModel: LyricViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentMovieBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_movie, container, false)
        binding.lyricViewModel = lyricViewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    companion object {
        const val MOVIE = "movie"
        const val SERIE = "serie"
    }
}