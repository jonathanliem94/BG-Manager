package com.jonathanl.bgmanager.ui.boardgamedetails

import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.useCases.NetworkUseCase

class BoardGameDetailsViewModel(
    private val networkUseCase: NetworkUseCase
) : ViewModel() {

    fun getBoardGameDetails(gameId: String) {

    }


    override fun onCleared() {
        super.onCleared()
    }

}