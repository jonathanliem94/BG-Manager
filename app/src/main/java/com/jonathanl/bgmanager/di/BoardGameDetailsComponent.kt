package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsFragment
import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [BoardGameDetailsModule::class],
    dependencies = [MainActivityComponent::class]
)
@FragmentScope
interface BoardGameDetailsComponent {

    fun inject(boardGameDetailsFragment: BoardGameDetailsFragment)

}

@Module
object BoardGameDetailsModule {

    @Provides
    @FragmentScope
    fun provideBoardGameDetailsViewModel(
        networkUseCase: NetworkUseCase,
        gameListUseCase: GameListUseCase
    ): BoardGameDetailsViewModel =
        BoardGameDetailsViewModel(
            networkUseCase,
            gameListUseCase
        )

}