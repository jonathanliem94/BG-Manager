package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.DBRepository
import com.jonathanl.bgmanager.data.models.GameListEntry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class GameListUseCaseTest {

    private val DBRepository: DBRepository = mock()
    private var gameListUseCaseUnderTest: GameListUseCase = GameListUseCaseImpl(DBRepository)

    @Test
    fun `when a game entry is added, ensure the repository adds it to the DB`() {
        val testEntry = GameListEntry("123", "test")
        gameListUseCaseUnderTest.handleNewGameEntry(testEntry)
        verify(DBRepository).insertSingleGameListEntry(testEntry)
    }

    @Test
    fun `when saving the game list, ensure the repository makes the correct call`() {
        val testGameList = mutableListOf(GameListEntry("456", "mock"))
        gameListUseCaseUnderTest.saveGameListToDB(testGameList)
        verify(DBRepository).saveGameListToDB(testGameList)
    }

    @Test
    fun `when a game entry is removed, ensure the repository removes it from the DB`() {
        val testEntry = GameListEntry("123", "test")
        gameListUseCaseUnderTest.removeGameEntryToDb(testEntry)
        verify(DBRepository).removeGameEntryToDb(testEntry)
    }

}