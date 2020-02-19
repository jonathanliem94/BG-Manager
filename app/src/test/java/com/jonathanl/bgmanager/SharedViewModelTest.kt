package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.network.BoardGameName
import com.jonathanl.bgmanager.network.BoardGameResult
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
import com.jonathanl.bgmanager.ui.gamelist.GameListEntry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Test

class SharedViewModelTest {

    private val repository: Repository = mock()
    private var sharedViewModelUnderTest: SharedViewModel = SharedViewModel(repository)

    @Test
    fun `when a duplicate game entry is inputted, the game list should not change`() {
        sharedViewModelUnderTest = SharedViewModel(repository)
        val entry = GameListEntry("test", "123")

        repeat(2) {
            sharedViewModelUnderTest.newGameListEntryHolder.onNext(entry)
        }

        sharedViewModelUnderTest.gameListHolder.test()
            .assertValue(mutableListOf(entry))
    }

    @Test
    fun `when a new game entry is inputted, the new game will be added to the game list`() {
        sharedViewModelUnderTest = SharedViewModel(repository)
        val entry1 = GameListEntry("test", "123")
        val entry2 = GameListEntry("hello", "456")

        sharedViewModelUnderTest.newGameListEntryHolder.onNext(entry1)
        sharedViewModelUnderTest.newGameListEntryHolder.onNext(entry2)

        sharedViewModelUnderTest.gameListHolder.test()
            .assertValue(mutableListOf(entry1, entry2))
            //.assertResult(mutableListOf(entry1), mutableListOf(entry1, entry2))
    }

    @Test
    fun `when the search query is blank, no search will be conducted`() {
        sharedViewModelUnderTest.searchQueryPublishSubject.onNext("")

        //await needed due to debounce part of subscription
        sharedViewModelUnderTest.searchResults.test()
            .awaitCount(1)
            .assertEmpty()
    }


    @Test
    fun `when the search query is a valid string, search will be conducted and then emitted properly`() {
        val testSearchQuery = "Pandemic"
        val fakeResult = BoardGameSearchResults(
            total = "5",
            termsofuse = "placeholder",
            resultsArray = listOf(BoardGameResult(
                "bg","123", BoardGameName("bg", "Panic!")
            ))
        )
        whenever(repository.makeBoardGameSearch(any())).thenReturn(Observable.just(fakeResult))

        sharedViewModelUnderTest.searchQueryPublishSubject.onNext(testSearchQuery)

        //await needed due to debounce part of subscription
        sharedViewModelUnderTest.searchResults.test()
            .awaitCount(1)
            .assertValue(fakeResult)
    }

}