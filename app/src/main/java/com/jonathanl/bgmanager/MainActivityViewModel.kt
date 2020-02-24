package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.useCases.NetworkUseCase

class MainActivityViewModel(
    private val networkUseCase: NetworkUseCase
) {

    fun conductBoardGameSearch(searchQuery: String?) {
        if (searchQuery != null) {
            networkUseCase.onSubmitQueryForBoardGameSearch(searchQuery)
        }
    }

}