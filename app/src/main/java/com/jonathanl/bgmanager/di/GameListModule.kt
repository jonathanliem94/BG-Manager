package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.data.*
import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsViewModel
import com.jonathanl.bgmanager.ui.gamelist.GameListViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.GameListUseCaseImpl
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object GameListModule {

    @Provides
    fun provideDBRepository(
        gameListDbService: GameListDbService
    ): DBRepository =
        DBRepositoryImpl(gameListDbService)

    @Provides
    fun provideGameListUseCase(dbRepository: DBRepository): GameListUseCase =
        GameListUseCaseImpl(dbRepository)

    @Provides
    fun providesGameListViewModel(
        gameListUseCase: GameListUseCase
    ): GameListViewModel =
        GameListViewModel(gameListUseCase)
}