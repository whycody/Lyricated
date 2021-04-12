package com.whycody.wordslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whycody.wordslife.home.HomeFragment
import com.whycody.wordslife.search.SearchFragment

class MainActivity : AppCompatActivity(), MainNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) showHomeFragment()
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        if(fragment !is IOnBackPressed || fragment.onBackPressed()) super.onBackPressed()
    }

    private fun showHomeFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, HomeFragment())
                .commit()
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim, R.anim.enter_anim, R.anim.exit_anim)
                .add(R.id.container, fragment)
        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun fragmentsContainSearchFragment() =
        supportFragmentManager.fragments.find { it is SearchFragment } != null
}