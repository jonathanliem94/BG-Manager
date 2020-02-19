package com.jonathanl.bgmanager.base

import androidx.fragment.app.Fragment
import com.jonathanl.bgmanager.di.MainActivityComponent

abstract class BaseFragment: Fragment() {

    private val baseActivity by lazy {
        activity as BaseActivity
    }

    fun getMainActivityComponent(): MainActivityComponent =
        baseActivity.getMainActivityComponent()

}