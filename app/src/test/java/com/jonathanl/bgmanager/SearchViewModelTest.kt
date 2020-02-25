package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SearchViewModelTest {

    private val networkUseCase: NetworkUseCase = mock()
    private val gameListUseCase: GameListUseCase = mock()
    private val searchViewModelUnderTest: SearchViewModel = SearchViewModel(networkUseCase, gameListUseCase)

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

    @Test
    fun `when search query is null, there will not be an invocation to conduct a search`() {
        searchViewModelUnderTest.conductBoardGameSearch(null)
        verify(networkUseCase, never()).onSubmitQueryForBoardGameSearch(any())
    }

    @Test
    fun `when search query is valid, there will be an invocation to conduct a search`() {
        val testSearchQuery = "test123"
        searchViewModelUnderTest.conductBoardGameSearch(testSearchQuery)
        verify(networkUseCase).onSubmitQueryForBoardGameSearch(testSearchQuery)
    }

}