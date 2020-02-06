package com.jonathanl.bgmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
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
    var searchResults: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    // Disposable
    lateinit var disposable: Disposable

    init {
        subscribeToSearchQueryPublishSubject()
    }

    private fun subscribeToSearchQueryPublishSubject(){
        disposable = searchQueryPublishSubject
            .filter{it.isNotBlank()}
            .debounce(1, TimeUnit.SECONDS)
            .switchMap { query ->
                repository.makeBoardGameSearch(query)
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = { searchResults.onNext(it)},
                onError = {
                    Log.e("SharedViewModel", "subscribeToSearchQueryPublishSubject failed. $it")
                }
            )

    }


    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}