package com.whycody.wordslife.home.banner.lyricated

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentLyricatedBannerBinding

class LyricatedBannerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentLyricatedBannerBinding.inflate(inflater)
        val pulseAnim = AnimationUtils.loadAnimation(context, R.anim.pulse_anim)
        binding.astronautIllustration.startAnimation(pulseAnim)
        return binding.root
    }

}