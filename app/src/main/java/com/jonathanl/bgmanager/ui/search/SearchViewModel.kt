package com.jonathanl.bgmanager.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.data.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchViewModel(
    private val networkUseCase: NetworkUseCase,
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val boardGameSearchResults = networkUseCase.boardGameSearchResults

    fun addNewEntryToGameList(newGameListEntry: GameListEntry) {
        val disposable = gameListUseCase.handleNewGameEntry(newGameListEntry)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onComplete = {
                    Log.d("SearchViewModel", "addNewEntryToGameList success.")
                },
                onError = {
                    Log.e("SearchViewModel", "addNewEntryToGameList failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun conductBoardGameSearch(searchQuery: String?) {
        if (searchQuery?.isNotBlank() == true) {
            networkUseCase.onSubmitQueryForBoardGameSearch(searchQuery)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}