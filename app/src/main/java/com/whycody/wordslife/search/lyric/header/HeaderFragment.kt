package com.whycody.wordslife.search.lyric.header

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.databinding.FragmentHeaderBinding

class HeaderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentHeaderBinding.inflate(inflater)
        binding.header = arguments?.getString(HEADER, "")!!
        return binding.root
    }

    companion object {
        const val HEADER = "header"
        fun newInstance(header: String) =
                HeaderFragment().apply {
                    arguments = Bundle().apply {
                        putString(HEADER, header)
                    }
                }
    }
}