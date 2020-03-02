package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.data.models.GameListEntry
import com.jonathanl.bgmanager.ui.gamelist.GameListViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Test

class GameListViewModelTest {

    private val gameListUseCase: GameListUseCase = mock()
    private val gameListViewModelUnderTest =
        GameListViewModel(gameListUseCase)

    @Test
    fun `when game list is being saved, ensure db is called`() {
        whenever(gameListUseCase.saveGameListToDB(any())).thenReturn(Completable.complete())
        val testGameList = mutableListOf(GameListEntry("456", "mock"))
        gameListViewModelUnderTest.saveGameListToDb(testGameList)
        verify(gameListUseCase).saveGameListToDB(testGameList)
    }

    @Test
    fun `when an entry from the game list is being removed, ensure db is also updated`() {
        whenever(gameListUseCase.removeGameEntryToDb(any())).thenReturn(Completable.complete())
        val testEntry = GameListEntry("123", "test")
        gameListViewModelUnderTest.removeGameEntryFromDb(testEntry)
        verify(gameListUseCase).removeGameEntryToDb(testEntry)
    }
}