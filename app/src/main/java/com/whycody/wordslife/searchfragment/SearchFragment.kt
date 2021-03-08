package com.whycody.wordslife.searchfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.whycody.wordslife.R
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.searchfragment.history.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private lateinit var bannerStarsOne: ImageView
    private lateinit var bannerStarsTwo: ImageView
    private lateinit var bannerStarsThree: ImageView
    private lateinit var bannerStarsFour: ImageView
    private val languageDao: LanguageDao by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        val view = binding.root
        bannerStarsOne = view.bannerStarsOne
        bannerStarsTwo = view.bannerStarsTwo
        bannerStarsThree = view.bannerStarsThree
        bannerStarsFour = view.bannerStarsFour
        setupRecycler(binding)
        startAnimations()
        return view
    }

    private fun setupRecycler(binding: FragmentSearchBinding) {
        val historyAdapter = HistoryAdapter()
        with(binding.root.historyRecycler) {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = historyAdapter
        }
        putExampleDataToHistoryAdapter(historyAdapter)
        binding.historyDisponible = true
    }

    private fun putExampleDataToHistoryAdapter(historyAdapter: HistoryAdapter) {
        historyAdapter.submitList(listOf(
                HistoryItem(0, "jabłko", languageDao.getLanguage(LanguageDaoImpl.PL)!!.drawable,
                        languageDao.getLanguage(LanguageDaoImpl.ENG)!!.drawable, true),
                HistoryItem(1, "apple", languageDao.getLanguage(LanguageDaoImpl.ENG)!!.drawable,
                        languageDao.getLanguage(LanguageDaoImpl.PL)!!.drawable),
                HistoryItem(2, "manzana", languageDao.getLanguage(LanguageDaoImpl.ESP)!!.drawable,
                        languageDao.getLanguage(LanguageDaoImpl.ENG)!!.drawable)))
    }

    private fun startAnimations() {
        val animationOne = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_one)
        val animationTwo = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_two)
        val animationThree = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_three)
        val animationFour = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_four)
        bannerStarsOne.startAnimation(animationOne)
        bannerStarsTwo.startAnimation(animationTwo)
        bannerStarsThree.startAnimation(animationThree)
        bannerStarsFour.startAnimation(animationFour)
    }

}