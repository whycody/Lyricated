package com.whycody.wordslife.home.banner.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentMainBannerBinding

class MainBannerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBannerBinding.inflate(inflater)
        startAnimations(binding)
        return binding.root
    }

    private fun startAnimations(binding: FragmentMainBannerBinding) {
        val context = activity?.applicationContext
        with(binding) {
            bannerStarsOne.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_one))
            bannerStarsTwo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_two))
            bannerStarsThree.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_three))
            bannerStarsFour.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_stars_four))
        }
    }
}