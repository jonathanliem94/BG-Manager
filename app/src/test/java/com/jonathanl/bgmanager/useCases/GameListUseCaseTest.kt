package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.data.Repository
import com.jonathanl.bgmanager.data.models.GameListEntry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class GameListUseCaseTest {

    private val repository: Repository = mock()
    private var gameListUseCaseUnderTest: GameListUseCase = GameListUseCaseImpl(repository)

    @Test
    fun `when a game entry is added, ensure the repository adds it to the DB`() {
        val testEntry = GameListEntry("123", "test")
        gameListUseCaseUnderTest.handleNewGameEntry(testEntry)
        verify(repository).insertSingleGameListEntry(testEntry)
    }

    @Test
    fun `when saving the game list, ensure the repository makes the correct call`() {
        val testGameList = mutableListOf(GameListEntry("456", "mock"))
        gameListUseCaseUnderTest.saveGameListToDB(testGameList)
        verify(repository).saveGameListToDB(testGameList)
    }

    @Test
    fun `when a game entry is removed, ensure the repository removes it from the DB`() {
        val testEntry = GameListEntry("123", "test")
        gameListUseCaseUnderTest.removeGameEntryToDb(testEntry)
        verify(repository).removeGameEntryToDb(testEntry)
    }

}