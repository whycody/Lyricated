package com.whycody.wordslife.library.most.viewed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentLibraryHeaderBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryHeaderFragment : Fragment() {

    private lateinit var binding: FragmentLibraryHeaderBinding
    private lateinit var movieAnim: Animation
    private val viewModel: LibraryHeaderViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLibraryHeaderBinding.inflate(inflater)
        movieAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.movie_anim)
        observeMovie()
        return binding.root
    }

    private fun observeMovie() =
        viewModel.randomMovieTitle.observe(requireActivity()) { updateMovieTitle(it) }

    private fun updateMovieTitle(movieTitle: String) {
        MainScope().launch {
            binding.movieView.startAnimation(movieAnim)
            delay(400)
            binding.movie = movieTitle
        }
    }

}