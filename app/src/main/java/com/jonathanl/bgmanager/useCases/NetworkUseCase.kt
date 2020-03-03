package com.jonathanl.bgmanager.useCases

import android.util.Log
import com.jonathanl.bgmanager.data.Repository
import com.jonathanl.bgmanager.data.models.BoardGameDetailsHolder
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

const val SEARCH_START = 1

interface  NetworkUseCase {

    val boardGameSearchResults: Observable<BoardGameSearchResults>

    fun onSubmitQueryForBoardGameSearch(searchQuery: String): Disposable
    fun onSubmitQueryForBoardGameDetails(gameId: String): Single<BoardGameDetailsHolder>

}

class NetworkUseCaseImpl(
    private val repository: Repository
): NetworkUseCase {

    // BoardGame SearchResults observable
    private val boardGameSearchResultsBehaviour: BehaviorSubject<BoardGameSearchResults> = BehaviorSubject.create()
    override val boardGameSearchResults: Observable<BoardGameSearchResults> = boardGameSearchResultsBehaviour.hide()

    override fun onSubmitQueryForBoardGameSearch(searchQuery: String): Disposable {
         return repository.getBoardGameSearchResults(searchQuery)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onSuccess = {
                    boardGameSearchResultsBehaviour.onNext(it)
                },
                onError = {
                    Log.e("NetworkUseCase", "onSubmitQueryForBoardGameSearch failed. $it")
                }
            )
    }

    override fun onSubmitQueryForBoardGameDetails(gameId: String): Single<BoardGameDetailsHolder> {
        return repository.getBoardGameDetailsResults(gameId)
    }

}