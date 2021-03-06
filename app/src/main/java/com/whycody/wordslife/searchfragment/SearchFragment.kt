package com.whycody.wordslife.searchfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.whycody.wordslife.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var bannerStarsOne: ImageView
    private lateinit var bannerStarsTwo: ImageView
    private lateinit var bannerStarsThree: ImageView
    private lateinit var bannerStarsFour: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        bannerStarsOne = view.bannerStarsOne
        bannerStarsTwo = view.bannerStarsTwo
        bannerStarsThree = view.bannerStarsThree
        bannerStarsFour = view.bannerStarsFour
        startAnimations()
        return view
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