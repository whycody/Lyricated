package com.whycody.wordslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whycody.wordslife.home.HomeFragment

class MainActivity : AppCompatActivity(), MainNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) showHomeFragment()
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
                .add(R.id.container, fragment)
        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commit()
    }
}