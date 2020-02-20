package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.network.BoardGameName
import com.jonathanl.bgmanager.network.BoardGameResult
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.network.NetworkService
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
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

    @Before
    fun setUp() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Observable.just(fakeResult))
    }

    @Test
    fun `when the search query is blank, no search will be conducted`() {
        val emptySearchQuery  = ""

        networkUseCaseUnderTest.onSubmitSearchQuery(emptySearchQuery)

        verifyZeroInteractions(networkService)
        networkUseCaseUnderTest.searchStatus.test()
            .assertEmpty()
    }

    @Ignore("search status not emitting properly")
    @Test
    fun `when the search query is conducted, search status is emitted as SEARCH_START`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Observable.empty())

        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        networkUseCaseUnderTest.searchStatus.test()
            .assertValue(SEARCH_START)
    }

    @Ignore("search status not emitting properly")
    @Test
    fun `when search ends, search status is emitted correctly for zero results`() {
        whenever(networkService.getBoardGameSearchResults(testSearchQuery))
            .thenReturn(Observable.just(noResult))

        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == noResult}
        networkUseCaseUnderTest.searchStatus.test()
            .assertValue(NO_RESULT)

    }

    @Ignore("search status not emitting properly")
    @Test
    fun `when search ends, search status is emitted correctly for normal results`() {
        networkUseCaseUnderTest.onSubmitSearchQuery(testSearchQuery)

        networkUseCaseUnderTest.boardGameSearchResults.test()
            .assertValue{it == fakeResult}
        networkUseCaseUnderTest.searchStatus.test()
            .assertValue(NORMAL_RESULT)

    }

}