package com.whycody.lyricated.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.google.android.material.navigation.NavigationBarView
import com.whycody.lyricated.R
import com.whycody.lyricated.databinding.FragmentMainBinding
import com.whycody.lyricated.home.HomeFragment
import com.whycody.lyricated.library.LibraryFragment

class MainFragment : Fragment(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: FragmentMainBinding
    private val homeFragment = HomeFragment()
    private val libraryFragment = LibraryFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.mainNav.setOnItemSelectedListener(this)
        if(savedInstanceState == null)
            onNavigationItemSelected(binding.mainNav.menu[0])
        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setCurrentFragment(item.itemId)
        return true
    }

    private fun setCurrentFragment(menuId: Int) {
        val fragManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragManager.beginTransaction()
        val curFrag = fragManager.primaryNavigationFragment
        if(curFrag != null) fragmentTransaction.hide(curFrag)
        var frag = fragManager.findFragmentByTag(menuId.toString())
        if(frag == null) {
            frag = if(menuId == R.id.search) homeFragment else libraryFragment
            fragmentTransaction.add(R.id.mainFrameLayout, frag, menuId.toString())
        } else fragmentTransaction.show(frag)

        fragmentTransaction.setPrimaryNavigationFragment(frag)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()
    }

}