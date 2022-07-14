package com.whycody.lyricated.main.startup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.whycody.lyricated.IOnBackPressed
import com.whycody.lyricated.R
import com.whycody.lyricated.databinding.FragmentStartupBinding

class StartupFragment : Fragment(), IOnBackPressed {

    private lateinit var binding: FragmentStartupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStartupBinding.inflate(inflater)
        binding.astronautImageView.startAnimation(AnimationUtils
            .loadAnimation(requireContext(), R.anim.pulse_anim))
        binding.root.setOnClickListener { }
        binding.getStartedBtn.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.getStartedBtn.startAnimation(AnimationUtils
            .loadAnimation(requireContext(), R.anim.enter_down_up_anim))
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        return false
    }
}