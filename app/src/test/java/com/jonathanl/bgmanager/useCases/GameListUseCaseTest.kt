package com.jonathanl.bgmanager.useCases

import com.jonathanl.bgmanager.ui.gamelist.GameListEntry
import org.junit.Test

class GameListUseCaseTest {

    private var gameListUseCaseUnderTest: GameListUseCase = GameListUseCaseImpl()

    @Test
    fun `when a duplicate game entry is inputted, the game list should not change`() {
        val entry = GameListEntry("test", "123")

        repeat(2) {
            gameListUseCaseUnderTest.handleNewGameEntry(entry)
        }

        gameListUseCaseUnderTest.gameListHolder.test()
            .assertValue(mutableListOf(entry))
    }

    @Test
    fun `when a new game entry is inputted, the new game will be added to the game list`() {
        val entry1 = GameListEntry("test", "123")
        val entry2 = GameListEntry("hello", "456")

        gameListUseCaseUnderTest.handleNewGameEntry(entry1)
        gameListUseCaseUnderTest.handleNewGameEntry(entry2)

        gameListUseCaseUnderTest.gameListHolder.test()
            .assertValue(mutableListOf(entry1, entry2))
    }

}