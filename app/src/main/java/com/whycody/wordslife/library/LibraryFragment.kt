package com.whycody.wordslife.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentLibraryBinding.inflate(inflater)
        return binding.root
    }

}