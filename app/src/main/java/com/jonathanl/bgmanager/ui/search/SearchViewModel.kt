package com.jonathanl.bgmanager.ui.search

import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase

class SearchViewModel(
    private val networkUseCase: NetworkUseCase,
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    val boardGameSearchResults = networkUseCase.boardGameSearchResults
    val boardGameSearchStatus = networkUseCase.searchStatus

    fun addNewEntryToGameList(newGameListEntry: GameListEntry) {
        gameListUseCase.handleNewGameEntry(newGameListEntry)
    }

    fun conductBoardGameSearch(searchQuery: String?) {
        if (searchQuery != null) {
            networkUseCase.onSubmitQueryForBoardGameSearch(searchQuery)
        }
    }
}