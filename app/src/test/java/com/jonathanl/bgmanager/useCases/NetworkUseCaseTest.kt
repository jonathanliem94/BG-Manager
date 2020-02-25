package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.NetworkService
import com.jonathanl.bgmanager.data.models.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class NetworkUseCaseTest {

    private val networkService: NetworkService = mock()
    private val networkUseCaseUnderTest: NetworkUseCase = NetworkUseCaseImpl(networkService)

    private val testSearchQuery = "test"
    private val fakeSearchResult =
        BoardGameSearchResults(
            total = "1",
            resultsArray = listOf(
                BoardGameResult(
                    "123",
                    BoardGameName(
                        "Panic!"
                    )
                )
            )
        )
    private val testGameId = "123"
    private val testBoardGameInfo = BoardGameInfo(
        BoardGameData(
            yearPublished = "2020",
            description = "test123"
        )
    )

    @Test
    fun `when the search query is blank, no search will be conducted`() {
        val emptySearchQuery  = ""

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(emptySearchQuery)

        verifyZeroInteractions(networkService)
        networkUseCaseUnderTest.searchStatus.test()
            .assertEmpty()
    }

    @Test
    fun `when the search query is conducted, search status is emitted as SEARCH_START, and search result is emitted correctly`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(fakeSearchResult))
        val testObservableSearchStatus = networkUseCaseUnderTest.searchStatus.test()
        val testObservableSearchResult = networkUseCaseUnderTest.boardGameSearchResults.test()

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        testObservableSearchStatus.assertValue(SEARCH_START)
        testObservableSearchResult.awaitCount(1).assertValue(fakeSearchResult)
    }

    @Test
    fun `when the game id is blank, request for game details will not be invoked`() {
        val emptyGameId  = ""

        networkUseCaseUnderTest.onSubmitQueryForBoardGameDetails(emptyGameId)

        verifyZeroInteractions(networkService)
    }

    @Test
    fun `when game id is valid, request for game details will be invoked`() {
        whenever(networkService.getBoardGameInfo(testGameId))
            .thenReturn(Single.just(testBoardGameInfo))
        val testObservable = networkUseCaseUnderTest.boardGameDetails.test()

        networkUseCaseUnderTest.onSubmitQueryForBoardGameDetails(testGameId)

        testObservable
            .awaitCount(1)
            .assertValue(testBoardGameInfo)

    }

}