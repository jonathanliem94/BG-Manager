package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.data.*
import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object BoardGameDetailsModule {

    @Provides
    fun provideNetworkService(retrofitBuilder: Retrofit): NetworkService {
        return retrofitBuilder.create(NetworkService::class.java)
    }

    @Provides
    fun provideNetworkUseCase(bbgRepository: BGGRepository): NetworkUseCase =
        NetworkUseCaseImpl(bbgRepository)

    @Provides
    fun provideBGGRepository(
        networkService: NetworkService
    ): BGGRepository =
        BGGRepositoryImpl(networkService)

    @Provides
    fun providesBoardGameDetailsViewModel(
        networkUseCase: NetworkUseCase,
        gameListUseCase: GameListUseCase
    ): BoardGameDetailsViewModel =
        BoardGameDetailsViewModel(networkUseCase, gameListUseCase)

}