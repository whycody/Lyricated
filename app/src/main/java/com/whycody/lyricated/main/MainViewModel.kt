package com.whycody.lyricated.main

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.lyricated.data.Movie
import com.whycody.lyricated.data.api.ApiService
import com.whycody.lyricated.data.movie.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel(context: Context, private val apiService: ApiService,
    private val movieRepository: MovieRepository): ViewModel() {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val prefsEditor = sharedPrefs.edit()

    fun checkDatabase() {
        viewModelScope.launch {
            val localDatabaseVersion = getLocalDatabaseVersion()
            val response = apiService.getDatabaseVersion()
            if(response.isSuccessful) {
                val currentDatabaseVersion = response.body()?.version!!
                if(localDatabaseVersion != currentDatabaseVersion)
                    updateMovies(currentDatabaseVersion)
            }
        }
    }

    private fun updateMovies(currentDatabaseVersion: Int) {
        viewModelScope.launch {
            val response = apiService.getAllMovies()
            if(response.isSuccessful) {
                movieRepository.deleteAllMovies()
                movieRepository.insertMovies(response.body()?.movies!!.map {
                    Movie(it.id, it.lang, it.type, it.minutes, it.netflixid, 0,
                        it.en, it.pl, it.esp, it.fr, it.ger, it.it, it.pt)
                })
                updateLocalDatabaseVersion(currentDatabaseVersion)
            }
        }
    }

    private fun getLocalDatabaseVersion() = sharedPrefs.getInt(DATABASE_VERSION, 0)

    private fun updateLocalDatabaseVersion(version: Int) {
        prefsEditor.putInt(DATABASE_VERSION, version)
        prefsEditor.commit()
    }

    companion object {
        const val DATABASE_VERSION = "database version"
    }
}