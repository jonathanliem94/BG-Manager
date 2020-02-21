package com.jonathanl.bgmanager

import android.view.View
import com.jonathanl.bgmanager.ui.gamelist.GameListEntry
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import com.jonathanl.bgmanager.useCases.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private val networkUseCase: NetworkUseCase = mock()
    private val gameListUseCase: GameListUseCase = mock()
    lateinit var searchViewModelUnderTest: SearchViewModel

    @Before
    fun setUp() {
        whenever(networkUseCase.searchStatus).thenReturn(Observable.just(SEARCH_START))
        searchViewModelUnderTest = SearchViewModel(networkUseCase, gameListUseCase)
    }

    @Test
    fun `when a gameEntry is to be added, the gameEntry will be passed on to the gameListUseCase`() {
        val testGameListEntry = GameListEntry("test", "123")
        searchViewModelUnderTest.addNewEntryToGameList(testGameListEntry)
        verify(gameListUseCase).handleNewGameEntry(testGameListEntry)
    }

    @Test
    fun `when search has just started, set visibility accordingly`() {
        val disposable = searchViewModelUnderTest.subscribeToSearchStatus()
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.textViewVisibility.get())
        disposable.dispose()
    }

    @Test
    fun `when search return no results, set visibility accordingly`() {
        searchViewModelUnderTest.setVisibilityAfterSearchWithNoResults()
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.textViewVisibility.get())
    }

    @Test
    fun `when search returns normally, set visibility accordingly`() {
        searchViewModelUnderTest.setVisibilityAfterSearchWithResults()
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.textViewVisibility.get())
    }

    @Test
    fun `RecyclerView is GONE, Progress Bar is GONE, TextView is VISIBLE, on initialisation`() {
        whenever(networkUseCase.searchStatus).thenReturn(Observable.empty())
        searchViewModelUnderTest = SearchViewModel(networkUseCase, gameListUseCase)
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.textViewVisibility.get())
    }

}