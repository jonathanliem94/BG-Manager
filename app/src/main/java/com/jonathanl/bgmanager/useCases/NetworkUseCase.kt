package com.jonathanl.bgmanager.useCases

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.network.NetworkService
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

const val SEARCH_START = 1

interface  NetworkUseCase {

    val searchStatus: Observable<Int>
    val boardGameSearchResults: Observable<BoardGameSearchResults>

    fun onSubmitSearchQuery(searchQuery: String)

}

class NetworkUseCaseImpl(
    private val networkService: NetworkService
): NetworkUseCase {

    // SearchStarted observable
    private val searchStatusPublisher: PublishSubject<Int> = PublishSubject.create()
    override val searchStatus = searchStatusPublisher.hide()

    // SearchResults observable
    private val boardGameSearchResultsBehaviour: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    override val boardGameSearchResults = boardGameSearchResultsBehaviour.hide()

    override fun onSubmitSearchQuery(searchQuery: String) {
        if (searchQuery.isNotBlank()){
            searchStatusPublisher.onNext(SEARCH_START)
            makeBoardGameSearch(searchQuery)
        }
    }

    @VisibleForTesting
    fun makeBoardGameSearch(gameName: String): Disposable {
        return networkService.getBoardGameSearchResults(gameName)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onSuccess = {
                    boardGameSearchResultsBehaviour.onNext(it)
                },
                onError = {
                    Log.e("NetworkUseCase", "makeBoardGameSearch failed. $it")
                }
            )
    }

}