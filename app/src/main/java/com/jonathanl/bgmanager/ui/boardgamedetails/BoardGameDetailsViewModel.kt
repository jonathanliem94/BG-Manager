package com.jonathanl.bgmanager.ui.boardgamedetails

import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.ui.gamelist.models.GameListEntry
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase

class BoardGameDetailsViewModel(
    private val networkUseCase: NetworkUseCase,
    private val gameListUseCase: GameListUseCase
) : ViewModel() {

    val boardGameDetails = networkUseCase.boardGameDetails

    fun getBoardGameDetails(gameId: String) {
        networkUseCase.onSubmitQueryForBoardGameDetails(gameId)
    }

    fun addToGameList(gameName: String, gameId: String) {
        if (gameName.isNotBlank()) {
            val newGameEntry = GameListEntry(gameName, gameId)
            gameListUseCase.handleNewGameEntry(newGameEntry)
        }
    }

}