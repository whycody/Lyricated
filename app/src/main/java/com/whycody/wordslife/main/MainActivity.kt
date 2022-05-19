package com.whycody.wordslife.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao
import com.whycody.wordslife.data.settings.SettingsDaoImpl
import com.whycody.wordslife.search.SearchFragment
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MainNavigation {

    private val mainViewModel: MainViewModel by viewModel()
    private val appConfigurationDao: AppConfigurationDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateAppearance()
        mainViewModel.checkDatabase()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) showHomeFragment()
    }

    private fun updateAppearance() {
        val currentAppConfig = appConfigurationDao.getAppConfiguration()
        val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val darkMode = AppCompatDelegate.MODE_NIGHT_YES
        val lightMode = AppCompatDelegate.MODE_NIGHT_NO
        when (currentAppConfig.appearance) {
            SettingsDaoImpl.LIGHT -> AppCompatDelegate.setDefaultNightMode(lightMode)
            SettingsDaoImpl.DARK -> AppCompatDelegate.setDefaultNightMode(darkMode)
            else -> AppCompatDelegate.setDefaultNightMode(defaultMode)
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        if(fragment !is IOnBackPressed || fragment.onBackPressed()) super.onBackPressed()
    }

    private fun showHomeFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MainFragment())
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