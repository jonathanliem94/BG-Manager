package com.jonathanl.bgmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SharedViewModel: ViewModel() {

    private val repository: Repository by lazy {
        Repository()
    }
    // Search Query Observable
    val searchQueryPublishSubject: PublishSubject<String> = PublishSubject.create()
    // Search Results
    var searchResults: PublishSubject<BoardGameSearchResults> = PublishSubject.create()
    // Disposable
    lateinit var disposable: Disposable

    init {
        subscribeToSearchQueryPublishSubject()
    }

    private fun subscribeToSearchQueryPublishSubject(){
        disposable = searchQueryPublishSubject
            .filter{it.isNotBlank()}
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeBy {
                query -> repository.makeBoardGameSearch(query)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy (
                        onNext = { searchResults.onNext(it)},
                        onError = {
                            Log.e("SharedViewModel", "subscribe to MakeBoardGameSearch failed. $it")
                        }
                    )
            }
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}