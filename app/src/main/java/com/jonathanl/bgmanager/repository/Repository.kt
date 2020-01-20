package com.jonathanl.bgmanager.repository

import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.network.NetworkService
import io.reactivex.Observable

class Repository {

    private val networkServiceImpl: NetworkService by lazy {
        NetworkService.createNetworkService()
    }

    fun makeBoardGameSearch(gameName: String): Observable<BoardGameSearchResults> {
        return networkServiceImpl.getBoardGameSearchResults(gameName)
    }

}