package com.whycody.wordslife.library.most.viewed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.movie.MovieDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryHeaderViewModel(private val movieDao: MovieDao): ViewModel() {

    private var allMovies = movieDao.getAllMovies()
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
            allMovies = movieDao.getAllMovies()
            randomMovieTitle.postValue("Enola Holmes")
        } else randomMovieTitle.postValue(allMovies.random().eng!!)

}