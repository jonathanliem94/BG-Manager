package com.jonathanl.bgmanager.ui.boardgamedetails

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.data.models.BoardGameDetailsHolder
import com.jonathanl.bgmanager.data.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BoardGameDetailsViewModel(
    private val networkUseCase: NetworkUseCase,
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getBoardGameDetails(gameId: String): Single<BoardGameDetailsHolder> {
        return networkUseCase.onSubmitQueryForBoardGameDetails(gameId)
    }

    fun addToGameList(gameName: String, gameId: String) {
        if (gameName.isNotBlank()) {
            val newGameEntry = GameListEntry(
                gameId,
                gameName
            )
            addNewEntryToGameList(newGameEntry)
        }
    }

    private fun addNewEntryToGameList(newGameListEntry: GameListEntry) {
        val disposable = gameListUseCase.handleNewGameEntry(newGameListEntry)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onComplete = {
                    Log.d("BoardGameDetailsVM", "addNewEntryToGameList success.")
                },
                onError = {
                    Log.e("BoardGameDetailsVM", "addNewEntryToGameList failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}