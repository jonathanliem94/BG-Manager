package com.jonathanl.bgmanager.ui.boardgamedetails

import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.useCases.NetworkUseCase

class BoardGameDetailsViewModel(
    private val networkUseCase: NetworkUseCase
) : ViewModel() {

    val boardGameDetails = networkUseCase.boardGameDetails

    fun getBoardGameDetails(gameId: String) {
        networkUseCase.onSubmitQueryForBoardGameDetails(gameId)
    }

}