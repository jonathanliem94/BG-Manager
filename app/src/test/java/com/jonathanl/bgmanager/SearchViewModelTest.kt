package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import com.jonathanl.bgmanager.useCases.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private val networkUseCase: NetworkUseCase = mock()
    private val gameListUseCase: GameListUseCase = mock()
    private lateinit var searchViewModelUnderTest: SearchViewModel

    @Before
    fun setUp() {
        whenever(networkUseCase.searchStatus).thenReturn(Observable.just(SEARCH_START))
        searchViewModelUnderTest = SearchViewModel(networkUseCase, gameListUseCase)
    }

    @Test
    fun `when a gameEntry is to be added, the gameEntry will be passed on to the gameListUseCase`() {
        val testGameListEntry =
            GameListEntry(
                "test",
                "123"
            )
        searchViewModelUnderTest.addNewEntryToGameList(testGameListEntry)
        verify(gameListUseCase).handleNewGameEntry(testGameListEntry)
    }

}