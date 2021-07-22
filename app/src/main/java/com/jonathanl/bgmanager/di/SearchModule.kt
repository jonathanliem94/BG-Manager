package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.data.BGGRepository
import com.jonathanl.bgmanager.data.BGGRepositoryImpl
import com.jonathanl.bgmanager.data.NetworkService
import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsViewModel
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object SearchModule {

    @Provides
    fun providesSearchViewModel(
        networkUseCase: NetworkUseCase,
        gameListUseCase: GameListUseCase
    ): SearchViewModel =
        SearchViewModel(networkUseCase, gameListUseCase)
}