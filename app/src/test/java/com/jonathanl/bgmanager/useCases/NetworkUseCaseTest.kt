package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.models.BoardGameName
import com.jonathanl.bgmanager.data.models.BoardGameResult
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import com.jonathanl.bgmanager.data.NetworkService
import com.nhaarman.mockitokotlin2.*
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
    private val noSearchResult =
        BoardGameSearchResults(
            total = "0",
            resultsArray = listOf()
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
    fun `when the game id is blank, call for details will not be conducted`() {
        val emptyGameId  = ""

        networkUseCaseUnderTest.onSubmitQueryForBoardGameDetails(emptyGameId)

        verifyZeroInteractions(networkService)
    }

    @Test
    fun `when the search query is conducted, search status is emitted as SEARCH_START`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(BoardGameSearchResults()))
        val testObservable = networkUseCaseUnderTest.searchStatus.test()

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        testObservable.assertValue(SEARCH_START)
    }

    @Test
    fun `when search ends, search status is emitted correctly for zero results`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(noSearchResult))

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == noSearchResult}

    }

    @Test
    fun `when search ends, search status is emitted correctly for normal results`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(fakeSearchResult))

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == fakeSearchResult}

    }

}