package com.jonathanl.bgmanager.ui.gamelist

import androidx.lifecycle.ViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase

class GameListViewModel(
    gameListUseCase: GameListUseCase
) : ViewModel() {

    val gameListHolder = gameListUseCase.gameListHolder

}