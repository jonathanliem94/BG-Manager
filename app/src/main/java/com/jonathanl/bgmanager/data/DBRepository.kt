package com.jonathanl.bgmanager.data

import com.jonathanl.bgmanager.data.models.GameListEntry
import io.reactivex.Completable
import io.reactivex.Observable

interface DBRepository {

    // Game List DB stuff
    fun getGameListEntries(): Observable<MutableList<GameListEntry>>
    fun insertSingleGameListEntry(newGameListEntry: GameListEntry): Completable
    fun saveGameListToDB(gameList: MutableList<GameListEntry>): Completable
    fun removeGameEntryToDb(gameListEntry: GameListEntry): Completable

}

class DBRepositoryImpl(
    private val gameListDbService: GameListDbService
): DBRepository{

    private val gameListDatabaseDAO = gameListDbService.getDatabaseAccessService()

    override fun getGameListEntries(): Observable<MutableList<GameListEntry>> {
        return gameListDatabaseDAO.getAllGameListEntry()
    }

    override fun insertSingleGameListEntry(newGameListEntry: GameListEntry): Completable {
        return gameListDatabaseDAO.insertGameListEntry(mutableListOf(newGameListEntry))
    }

    override fun saveGameListToDB(gameList: MutableList<GameListEntry>): Completable {
        return gameListDatabaseDAO.insertGameListEntry(gameList)
    }

    override fun removeGameEntryToDb(gameListEntry: GameListEntry): Completable {
        return gameListDatabaseDAO.deleteGameListEntry(gameListEntry)
    }

}