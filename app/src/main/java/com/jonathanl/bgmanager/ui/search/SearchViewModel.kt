package com.jonathanl.bgmanager.ui.home

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val text = ObservableField<String>()

    val progressBarVisibility = ObservableInt()

    val recyclerViewVisibility = ObservableInt()

    init {
        text.set("This is search Fragment")
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    fun setVisibilityDuringSearch() {
        recyclerViewVisibility.set(View.GONE)
        progressBarVisibility.set(View.VISIBLE)
    }

    fun setVisibilityAfterSearch() {
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

}