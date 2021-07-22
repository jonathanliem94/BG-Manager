package com.jonathanl.bgmanager.data

import com.jonathanl.bgmanager.data.models.BoardGameDetailsHolder
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import io.reactivex.Single

interface BGGRepository {

    // Network stuff
    fun getBoardGameSearchResults(query: String): Single<BoardGameSearchResults>
    fun getBoardGameDetailsResults(gameId: String): Single<BoardGameDetailsHolder>

}

class BGGRepositoryImpl(
    private val networkService: NetworkService
): BGGRepository{

    override fun getBoardGameDetailsResults(gameId: String): Single<BoardGameDetailsHolder> {
        return networkService.getBoardGameInfo(gameId)
    }

    override fun getBoardGameSearchResults(query: String): Single<BoardGameSearchResults> {
        return networkService.getBoardGameSearchResults(query)
    }

}