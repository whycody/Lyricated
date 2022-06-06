package com.whycody.wordslife.library.most.viewed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.movie.MovieRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryHeaderViewModel(private val movieRepository: MovieRepository): ViewModel() {

    private var allMovies = movieRepository.getAllMovies()
    val randomMovieTitle = MutableLiveData<String>()

    init {
        MainScope().launch {
            updateMovieTitle()
            while(true) refreshMovie()
        }
    }

    private suspend fun refreshMovie() {
        delay(5000)
        updateMovieTitle()
    }

    private fun updateMovieTitle() =
        if(allMovies.isEmpty()) {
            allMovies = movieRepository.getAllMovies()
            randomMovieTitle.postValue("Enola Holmes")
        } else randomMovieTitle.postValue(allMovies.random().eng!!)
}