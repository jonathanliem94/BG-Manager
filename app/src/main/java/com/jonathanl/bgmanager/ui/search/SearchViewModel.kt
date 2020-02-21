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

    val text = ObservableField<String>("Let's start a search!")
    val textViewVisibility = ObservableInt(View.VISIBLE)
    val progressBarVisibility = ObservableInt(View.INVISIBLE)
    val recyclerViewVisibility = ObservableInt(View.VISIBLE)
    val boardGameSearchResults = networkUseCase.boardGameSearchResults
    private val disposable: Disposable

    init {
        disposable = subscribeToSearchStatus()
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
                    }
                },
                onError = {
                    Log.e("SearchViewModel", "subscribeToSearchStatus failed. $it")
                }
            )
    }

    @VisibleForTesting
    fun setVisibilityDuringSearch() {
        text.set("Searching...")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.VISIBLE)
        recyclerViewVisibility.set(View.GONE)
    }

    fun setVisibilityAfterSearchWithResults() {
        textViewVisibility.set(View.GONE)
        progressBarVisibility.set(View.GONE)
        recyclerViewVisibility.set(View.VISIBLE)
    }

    fun setVisibilityAfterSearchWithNoResults() {
        text.set("Oops, there seems to be no results!")
        textViewVisibility.set(View.VISIBLE)
        progressBarVisibility.set(View.INVISIBLE)
        recyclerViewVisibility.set(View.GONE)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}