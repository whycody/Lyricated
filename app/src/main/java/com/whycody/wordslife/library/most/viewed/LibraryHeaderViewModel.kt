package com.whycody.wordslife.library.most.viewed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.MovieApi
import com.whycody.wordslife.data.api.ApiService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryHeaderViewModel(private val apiService: ApiService): ViewModel() {

    private val allMovies = MutableLiveData<List<MovieApi>>()
    val randomMovie = MutableLiveData<MovieApi>()

    init {
        MainScope().launch {
            val allMoviesResponse = apiService.getAllMovies().body()
            allMovies.value = allMoviesResponse?.movies
            randomMovie.postValue(allMovies.value?.random())
            while(true) refreshMovie()
        }
    }

    private suspend fun refreshMovie() {
        delay(5000)
        randomMovie.postValue(allMovies.value?.random())
    }
}