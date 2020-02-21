package com.jonathanl.bgmanager.useCases

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.jonathanl.bgmanager.network.models.BoardGameSearchResults
import com.jonathanl.bgmanager.network.NetworkService
import com.jonathanl.bgmanager.network.models.BoardGameInfo
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
    val boardGameDetails: Observable<BoardGameInfo>

    fun onSubmitQueryForBoardGameSearch(searchQuery: String)
    fun onSubmitQueryForBoardGameDetails(searchQuery: String)

}

class NetworkUseCaseImpl(
    private val networkService: NetworkService
): NetworkUseCase {

    // SearchStarted observable
    private val searchStatusPublisher: PublishSubject<Int> = PublishSubject.create()
    override val searchStatus = searchStatusPublisher.hide()

    // BoardGame SearchResults observable
    private val boardGameSearchResultsBehaviour: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    override val boardGameSearchResults = boardGameSearchResultsBehaviour.hide()

    // BoardGameDetails Results observable
    private val boardGameDetailsPublish: PublishSubject<BoardGameInfo> = PublishSubject.create()
    override val boardGameDetails = boardGameDetailsPublish.hide()

    override fun onSubmitQueryForBoardGameSearch(searchQuery: String) {
        if (searchQuery.isNotBlank()){
            searchStatusPublisher.onNext(SEARCH_START)
            makeBoardGameSearch(searchQuery)
        }
    }

    override fun onSubmitQueryForBoardGameDetails(searchQuery: String) {
        if (searchQuery.isNotBlank()){
            makeBoardGameDetailsSearch(searchQuery)
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

    @VisibleForTesting
    fun makeBoardGameDetailsSearch(gameName: String): Disposable {
        return networkService.getBoardGameInfo(gameName)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onSuccess = {
                    boardGameDetailsPublish.onNext(it)
                },
                onError = {
                    Log.e("NetworkUseCase", "makeBoardGameDetailsSearch failed. $it")
                }
            )
    }

}