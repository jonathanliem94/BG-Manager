package com.jonathanl.bgmanager.data

import com.jonathanl.bgmanager.data.models.BoardGameDetailsHolder
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://www.boardgamegeek.com"
const val BASE_API_1 = "/xmlapi"
const val BASE_API_2 = "/xmlapi2"

interface NetworkService {

    @GET("${BASE_API_2}/search")
    fun getBoardGameSearchResults(
        @Query("query") game_name: String,
        @Query("type") game_type: String = "boardgame"):
            Single<BoardGameSearchResults>

    @GET("${BASE_API_1}/boardgame/{gameId}")
    fun getBoardGameInfo(
        @Path("gameId") gameId: String,
        @Query("stats") stats: String = "0",
        @Query("historical") playStats: String = "0"
    ): Single<BoardGameDetailsHolder>

    companion object {
        fun createNetworkService(): NetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}

