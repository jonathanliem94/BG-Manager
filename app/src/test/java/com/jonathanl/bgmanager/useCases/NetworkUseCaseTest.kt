package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.DBRepository
import com.jonathanl.bgmanager.data.models.BoardGameSearchId
import com.jonathanl.bgmanager.data.models.BoardGameSearchName
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class NetworkUseCaseTest {

    private val DBRepository: DBRepository = mock()
    private val networkUseCaseUnderTest: NetworkUseCase = NetworkUseCaseImpl(DBRepository)

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
        whenever(DBRepository.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(fakeSearchResult))
        val testObservableSearchResult = networkUseCaseUnderTest.boardGameSearchResults.test()

        val disposable = networkUseCaseUnderTest.onSubmitQueryForBoardGameSearch(testSearchQuery)

        testObservableSearchResult.awaitCount(1).assertValue(fakeSearchResult)

        disposable.dispose()
    }

    @Test
    fun `when game id is valid, request for game details will be invoked`() {
        networkUseCaseUnderTest.onSubmitQueryForBoardGameDetails(testGameId)
        verify(DBRepository).getBoardGameDetailsResults(testGameId)
    }

}