package com.whycody.wordslife.search.filter.choose.source

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.search.filter.choose.source.recycler.MovieListItemAdapter
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.ActivityChooseSourceBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseSourceActivity : AppCompatActivity(), TextWatcher {

    private val searchConfDao: SearchConfigurationDao by inject()
    private val chooseSourceViewModel: ChooseSourceViewModel by viewModel()
    private lateinit var binding: ActivityChooseSourceBinding
    private var lastSearchConf = searchConfDao.getSearchConfiguration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSourceBinding.inflate(layoutInflater)
        setupRecycler(binding.movieRecycler)
        binding.searchMovieInput.addTextChangedListener(this)
        binding.clearBtn.setOnClickListener{ binding.searchMovieInput.setText("") }
        observeSearchConf()
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if((binding.movieRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) {
            binding.searchAppBar.setExpanded(true)
            binding.movieRecycler.smoothScrollToPosition(0)
        } else finish()
    }

    private fun setupRecycler(recycler: RecyclerView) {
        val adapter = MovieListItemAdapter(chooseSourceViewModel)
        recycler.adapter = adapter
        observeMovieListItems(adapter)
    }

    private fun observeMovieListItems(adapter: MovieListItemAdapter) =
        chooseSourceViewModel.getMovieListItems().observe(this) {
            adapter.submitList(it)
        }

    private fun observeSearchConf() {
        searchConfDao.getSearchConfigurationLiveData().observe(this) {
            val currentSearchConf = searchConfDao.getSearchConfiguration()
            if (currentSearchConf.chosenSource != lastSearchConf.chosenSource) finish()
            lastSearchConf = currentSearchConf
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        chooseSourceViewModel.searchTextChanged(s!!.toString())
        binding.clearBtn.visibility = if(s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
    }

    override fun afterTextChanged(s: Editable?) { }
}