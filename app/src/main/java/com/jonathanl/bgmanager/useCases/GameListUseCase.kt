package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.DBRepository
import com.jonathanl.bgmanager.data.models.GameListEntry
import io.reactivex.Completable
import io.reactivex.Observable

interface GameListUseCase {

    val gameListHolder: Observable<MutableList<GameListEntry>>
    fun handleNewGameEntry(newGameListEntry: GameListEntry): Completable
    fun saveGameListToDB(gameList: MutableList<GameListEntry>): Completable
    fun removeGameEntryToDb(gameListEntry: GameListEntry): Completable

}

class GameListUseCaseImpl(
    private val DBRepository: DBRepository
): GameListUseCase {

    // Game List Observable
    override val gameListHolder: Observable<MutableList<GameListEntry>> = DBRepository.getGameListEntries()

    override fun handleNewGameEntry(newGameListEntry: GameListEntry): Completable {
        return DBRepository.insertSingleGameListEntry(newGameListEntry)
    }

    override fun saveGameListToDB(gameList: MutableList<GameListEntry>): Completable {
        return DBRepository.saveGameListToDB(gameList)
    }

    override fun removeGameEntryToDb(gameListEntry: GameListEntry): Completable {
        return DBRepository.removeGameEntryToDb(gameListEntry)
    }

}