package com.jonathanl.bgmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
import com.jonathanl.bgmanager.ui.gamelist.GameListEntry
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SharedViewModel(
    private val repository: Repository
): ViewModel() {

    // Search Results
    // Behaviour subject, so that the old results will still be there when navigating between other parts of the app
    private val searchResultsBehavior: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    val searchResults = searchResultsBehavior.hide()
    // Game List Observable
    private val gameListBehavior: BehaviorSubject<MutableList<GameListEntry>> = BehaviorSubject.create()
    val gameListHolder = gameListBehavior.hide()

    // New entry to be added to GameList observable
    val newGameListEntryHolder: PublishSubject<GameListEntry> = PublishSubject.create()
    // Search Query Observable
    val searchQueryPublishSubject: PublishSubject<String> = PublishSubject.create()

    // composite disposable
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
            subscribeToSearchQueryPublishSubject(),
            subscribeToNewGameListEntryHolder()
        )
    }

    private fun subscribeToSearchQueryPublishSubject(): Disposable {
        return searchQueryPublishSubject
            .filter{it.isNotBlank()}
            .debounce(1, TimeUnit.SECONDS)
            .switchMap { query ->
                repository.makeBoardGameSearch(query)
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {
                    searchResultsBehavior.onNext(it)
                },
                onError = {
                    Log.e("SharedViewModel", "subscribeToSearchQueryPublishSubject failed. $it")
                }
            )
    }

    private fun subscribeToNewGameListEntryHolder(): Disposable {
        return newGameListEntryHolder
            .subscribeBy (
                onNext = {
                    addNewGameEntry(it)
                },
                onError = {
                    Log.e("SharedViewModel", "subscribeToNewGameListEntryHolder failed. $it")
                }
            )
    }

    private fun addNewGameEntry(newGameListEntry: GameListEntry) {
        val gameList = gameListBehavior.value ?: mutableListOf()
        if (gameList.contains(newGameListEntry)) {
            return
        }
        else{
            gameList.add(newGameListEntry)
            gameListBehavior.onNext(gameList)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}