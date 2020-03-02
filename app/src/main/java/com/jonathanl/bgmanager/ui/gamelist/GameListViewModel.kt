package com.jonathanl.bgmanager.ui.gamelist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.data.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class GameListViewModel(
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val gameListHolder = gameListUseCase.gameListHolder

    fun saveGameListToDb(gameList: MutableList<GameListEntry>){
        val disposable = gameListUseCase.saveGameListToDB(gameList)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onComplete = {
                    Log.d("GameListViewModel", "saveGameListToDb success.")
                },
                onError = {
                    Log.e("GameListViewModel", "saveGameListToDb failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun removeGameEntryFromDb(gameListEntry: GameListEntry) {
        val disposable = gameListUseCase.removeGameEntryToDb(gameListEntry)
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onComplete = {
                    Log.d("GameListViewModel", "removeGameEntryFromDb success.")
                },
                onError = {
                    Log.e("GameListViewModel", "removeGameEntryFromDb failed. $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}