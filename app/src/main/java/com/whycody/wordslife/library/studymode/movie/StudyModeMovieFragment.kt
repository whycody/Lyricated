package com.whycody.wordslife.library.studymode.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.data.utilities.MoviePlayer
import com.whycody.wordslife.databinding.FragmentStudyModeMovieBinding
import com.whycody.wordslife.library.studymode.StudyModeViewModel
import com.whycody.wordslife.search.lyric.movie.MovieInteractor
import com.whycody.wordslife.search.lyric.movie.MovieViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StudyModeMovieFragment : Fragment(), MovieInteractor {

    private lateinit var binding: FragmentStudyModeMovieBinding
    private val movieViewModel: MovieViewModel by inject()
    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()
    private val moviePlayer: MoviePlayer by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStudyModeMovieBinding.inflate(inflater)
        binding.interactor = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieViewModel = movieViewModel
        observeExtendedLyricItem()
        return binding.root
    }

    private fun observeExtendedLyricItem() {
        studyModeViewModel.getExtendedLyricItem().observe(viewLifecycleOwner) {
            if(it==null) return@observe
            movieViewModel.findMovie(it)
        }
    }

    override fun playBtnClicked(netflixid: Int, time: String) = moviePlayer.playMovie(netflixid, time)
}