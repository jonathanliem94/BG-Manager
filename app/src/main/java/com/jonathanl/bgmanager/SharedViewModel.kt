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

class SharedViewModel: ViewModel() {

    private val repository: Repository by lazy {
        Repository()
    }
    // Search Query Observable
    val searchQueryPublishSubject: PublishSubject<String> = PublishSubject.create()
    // Search Results
    // Behaviour subject, so that the old results will still be there when navigating between other parts of the app
    val searchResults: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    // Game List Observable
    val gameListHolder: BehaviorSubject<MutableList<GameListEntry>> = BehaviorSubject.create()
    // New entry to be added to GameList observable
    val newGameListEntryHolder: PublishSubject<GameListEntry> = PublishSubject.create()
    // composite disposable
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        subscribeToSearchQueryPublishSubject()
        subscribeToNewGameListEntryHolder()

    }

    private fun subscribeToSearchQueryPublishSubject(){
        val disposable = searchQueryPublishSubject
            .filter{it.isNotBlank()}
            .debounce(1, TimeUnit.SECONDS)
            .switchMap { query ->
                repository.makeBoardGameSearch(query)
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {
                    searchResults.onNext(it)
                },
                onError = {
                    Log.e("SharedViewModel", "subscribeToSearchQueryPublishSubject failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun subscribeToNewGameListEntryHolder(){
        val disposable = newGameListEntryHolder
            .subscribeBy (
                onNext = {
                    addNewGameEntry(it)
                },
                onError = {
                    Log.e("SharedViewModel", "subscribeToNewGameListEntryHolder failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun addNewGameEntry(newGameListEntry: GameListEntry){
        val gameList = gameListHolder.value ?: mutableListOf()
        if (gameList.contains(newGameListEntry)) {
            return
        }
        else{
            gameList.add(newGameListEntry)
            gameListHolder.onNext(gameList)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}