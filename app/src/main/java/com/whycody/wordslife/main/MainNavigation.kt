package com.whycody.wordslife.main

import androidx.fragment.app.Fragment

interface MainNavigation {

    fun navigateTo(fragment: Fragment, addToBackstack: Boolean = true)

    fun fragmentsContainSearchFragment(): Boolean
}