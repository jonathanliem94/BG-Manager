package com.jonathanl.bgmanager.ui.search

import android.util.Log
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.ui.gamelist.GameListEntry
import com.jonathanl.bgmanager.useCases.*
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class SearchViewModel(
    private val networkUseCase: NetworkUseCase,
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    val text = ObservableField<String>()
    val textViewVisibility = ObservableInt()
    val progressBarVisibility = ObservableInt()
    val recyclerViewVisibility = ObservableInt()
    val boardGameSearchResults = networkUseCase.boardGameSearchResults

    init {
        setVisibilityDuringInit()
        subscribeToSearchStatus()
    }

    fun addNewEntryToGameList(newGameListEntry: GameListEntry) {
        gameListUseCase.handleNewGameEntry(newGameListEntry)
    }

    @VisibleForTesting
    fun subscribeToSearchStatus(): Disposable {
        return networkUseCase.searchStatus
            .subscribeBy (
                onNext = {
                    when (it) {
                        SEARCH_START -> setVisibilityDuringSearch()
                        NO_RESULT -> setVisibilityAfterSearchWithNoResults()
                        NORMAL_RESULT -> setVisibilityAfterSearchWithResults()
                    }
                },
                onError = {
                    Log.e("SearchViewModel", "subscribeToSearchStatus failed. $it")
                }
            )
    }

    @VisibleForTesting
    fun setVisibilityDuringInit() {
        text.set("Let's start a search!")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    @VisibleForTesting
    fun setVisibilityDuringSearch() {
        text.set("Searching...")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.VISIBLE)
        recyclerViewVisibility.set(View.GONE)
    }

    @VisibleForTesting
    fun setVisibilityAfterSearchWithResults() {
        textViewVisibility.set(View.GONE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    @VisibleForTesting
    fun setVisibilityAfterSearchWithNoResults() {
        text.set("Oops, there seems to be no results!")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.GONE)
    }

}