package com.jonathanl.bgmanager.data

import com.jonathanl.bgmanager.data.models.BoardGameDetailsHolder
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import com.jonathanl.bgmanager.data.models.GameListEntry
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {

    // Game List DB stuff
    fun getGameListEntries(): Observable<MutableList<GameListEntry>>
    fun insertSingleGameListEntry(newGameListEntry: GameListEntry): Completable
    fun saveGameListToDB(gameList: MutableList<GameListEntry>): Completable
    fun removeGameEntryToDb(gameListEntry: GameListEntry): Completable

    // Network stuff
    fun getBoardGameSearchResults(query: String): Single<BoardGameSearchResults>
    fun getBoardGameDetailsResults(gameId: String): Single<BoardGameDetailsHolder>

}

class RepositoryImpl(
    private val gameListDbService: GameListDbService,
    private val networkService: NetworkService
): Repository{

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

    override fun getBoardGameDetailsResults(gameId: String): Single<BoardGameDetailsHolder> {
        return networkService.getBoardGameInfo(gameId)
    }

    override fun getBoardGameSearchResults(query: String): Single<BoardGameSearchResults> {
        return networkService.getBoardGameSearchResults(query)
    }

}