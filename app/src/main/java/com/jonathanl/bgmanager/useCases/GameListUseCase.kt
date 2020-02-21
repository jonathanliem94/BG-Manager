package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface GameListUseCase {

    val gameListHolder: Observable<MutableList<GameListEntry>>

    fun handleNewGameEntry(newGameListEntry: GameListEntry)
}

class GameListUseCaseImpl: GameListUseCase {

    // Game List Observable
    private val gameListBehavior: BehaviorSubject<MutableList<GameListEntry>> = BehaviorSubject.create()
    override val gameListHolder = gameListBehavior.hide()

    override fun handleNewGameEntry(newGameListEntry: GameListEntry) {
        val gameList = gameListBehavior.value ?: mutableListOf()
        if (gameList.contains(newGameListEntry)) {
            return
        }
        else{
            gameList.add(newGameListEntry)
            gameListBehavior.onNext(gameList)
        }
    }

}