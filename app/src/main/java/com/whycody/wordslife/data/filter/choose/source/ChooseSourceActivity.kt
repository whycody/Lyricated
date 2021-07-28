package com.whycody.wordslife.data.filter.choose.source

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.R
import com.whycody.wordslife.data.filter.choose.source.recycler.MovieListItemAdapter
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseSourceActivity : AppCompatActivity() {

    private val searchConfDao: SearchConfigurationDao by inject()
    private val chooseSourceViewModel: ChooseSourceViewModel by viewModel()
    private var lastSearchConf = searchConfDao.getSearchConfiguration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_source)
        setupRecycler(findViewById(R.id.movieRecycler))
        observeSearchConf()
    }

    private fun setupRecycler(recycler: RecyclerView) {
        val adapter = MovieListItemAdapter(chooseSourceViewModel)
        recycler.adapter = adapter
        observeMovieListItems(adapter)
    }

    private fun observeMovieListItems(adapter: MovieListItemAdapter) =
        chooseSourceViewModel.getMovieListItems().observe(this, {
            adapter.submitList(it)
        })

    private fun observeSearchConf() {
        searchConfDao.getSearchConfigurationLiveData().observe(this, {
            val currentSearchConf = searchConfDao.getSearchConfiguration()
            if(currentSearchConf.chosenSource != lastSearchConf.chosenSource) finish()
            lastSearchConf = currentSearchConf
        })
    }
}