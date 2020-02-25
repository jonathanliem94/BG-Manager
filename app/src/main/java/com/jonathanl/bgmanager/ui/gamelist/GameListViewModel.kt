package com.jonathanl.bgmanager.ui.gamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase

class GameListViewModel(
    gameListUseCase: GameListUseCase
) : ViewModel() {

    val gameListHolder = gameListUseCase.gameListHolder

    private val _text = MutableLiveData<String>().apply {
        value = "Your list is empty."
    }
    val text: LiveData<String> = _text

}