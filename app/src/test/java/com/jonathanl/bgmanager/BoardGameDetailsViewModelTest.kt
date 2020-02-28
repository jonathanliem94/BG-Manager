package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsViewModel
import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Test

class BoardGameDetailsViewModelTest {

    private val networkUseCase: NetworkUseCase = mock()
    private val gameListUseCase: GameListUseCase = mock()
    private val boardGameDetailsViewModelUnderTest: BoardGameDetailsViewModel = BoardGameDetailsViewModel(networkUseCase, gameListUseCase)
    private val testId = "123"

    @Test
    fun `ensure call for more details is invoked`() {
        boardGameDetailsViewModelUnderTest.getBoardGameDetails(testId)
        verify(networkUseCase).onSubmitQueryForBoardGameDetails(testId)
    }

    @Test
    fun `if game data is invalid, adding to the game list will not occur`() {
        val invalidName = ""
        boardGameDetailsViewModelUnderTest.addToGameList(invalidName, testId)
        verifyZeroInteractions(gameListUseCase)
    }

    @Test
    fun `if game data is valid, adding to the game list will be invoked`() {
        val testName = "testGame"
        boardGameDetailsViewModelUnderTest.addToGameList(testName, testId)
        verify(gameListUseCase).handleNewGameEntry(GameListEntry(testName,testId))
    }

}