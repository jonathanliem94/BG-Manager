package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.network.BoardGameName
import com.jonathanl.bgmanager.network.BoardGameResult
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.network.NetworkService
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class NetworkUseCaseTest {

    private val networkService: NetworkService = mock()
    private val networkUseCaseUnderTest: NetworkUseCase = NetworkUseCaseImpl(networkService)

    private val testSearchQuery = "test"
    private val fakeResult = BoardGameSearchResults(
        total = "1",
        termsofuse = "placeholder",
        resultsArray = listOf(BoardGameResult(
            "bg","123", BoardGameName("bg", "Panic!")
        ))
    )
    private val noResult = BoardGameSearchResults(
        total = "0",
        termsofuse = "placeholder",
        resultsArray = listOf()
    )

    @Test
    fun `when the search query is blank, no search will be conducted`() {
        val emptySearchQuery  = ""

        networkUseCaseUnderTest.onSubmitSearchQuery(emptySearchQuery)

        verifyZeroInteractions(networkService)
        networkUseCaseUnderTest.searchStatus.test()
            .assertEmpty()
    }

    @Test
    fun `when the search query is conducted, search status is emitted as SEARCH_START`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(BoardGameSearchResults()))
        val testObservable = networkUseCaseUnderTest.searchStatus.test()

        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        testObservable.assertValue(SEARCH_START)
    }

    @Test
    fun `when search ends, search status is emitted correctly for zero results`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(noResult))

        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == noResult}

    }

    @Test
    fun `when search ends, search status is emitted correctly for normal results`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Single.just(fakeResult))

        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == fakeResult}

    }

}