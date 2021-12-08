package com.whycody.wordslife.library.most.viewed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.movie.MovieDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MostViewedViewModel(private val movieDao: MovieDao): ViewModel() {

    val latestMovie = MutableLiveData(movieDao.getRandomMovie())

    init {
        MainScope().launch {
            while(true) refreshMovie()
        }
    }

    private suspend fun refreshMovie() {
        delay(5000)
        latestMovie.postValue(movieDao.getRandomMovie())
    }
}