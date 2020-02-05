package com.jonathanl.bgmanager.ui.search

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val text = ObservableField<String>()
    val textViewVisibility = ObservableInt()
    val progressBarVisibility = ObservableInt()
    val recyclerViewVisibility = ObservableInt()

    init {
        text.set("Let's start a search!")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    fun setVisibilityDuringSearch() {
        text.set("Searching...")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.VISIBLE)
        recyclerViewVisibility.set(View.GONE)
    }

    fun setVisibilityAfterSearchWithResults() {
        text.set("Let's start a search!")
        textViewVisibility.set(View.GONE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    fun setVisibilityAfterSearchWithNoResults() {
        text.set("Oops, there seems to be no results!")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.GONE)
    }

}