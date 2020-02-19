package com.jonathanl.bgmanager.base

import androidx.appcompat.app.AppCompatActivity
import com.jonathanl.bgmanager.di.DaggerMainActivityComponent
import com.jonathanl.bgmanager.di.MainActivityComponent

abstract class BaseActivity: AppCompatActivity() {

    private val component by lazy {
        DaggerMainActivityComponent.create()
    }

    fun getMainActivityComponent(): MainActivityComponent = component

}