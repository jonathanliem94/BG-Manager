package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.Repository
import com.jonathanl.bgmanager.data.models.BoardGameSearchId
import com.jonathanl.bgmanager.data.models.BoardGameSearchName
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class NetworkUseCaseTest {

    private val repository: Repository = mock()
    private val networkUseCaseUnderTest: NetworkUseCase = NetworkUseCaseImpl(repository)

    private val testSearchQuery = "test"
    private val fakeSearchResult =
        BoardGameSearchResults(
            total = "1",
            resultsArray = listOf(
                BoardGameSearchId(
                    "123",
                    BoardGameSearchName(
                        "Panic!"
                    )
                )
            )
        )
    private val testGameId = "123"

    @Test
    fun `when the search query is conducted, ensure search result is emitted from the behaviour subject correctly`() {
        whenever(repository.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(fakeSearchResult))
        val testObservableSearchResult = networkUseCaseUnderTest.boardGameSearchResults.test()

        networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        testObservableSearchResult.awaitCount(1).assertValue(fakeSearchResult)
    }

    @Test
    fun `when game id is valid, request for game details will be invoked`() {
        networkUseCaseUnderTest.onSubmitQueryForBoardGameDetails(testGameId)
        verify(repository).getBoardGameDetailsResults(testGameId)
    }

}