package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.ui.gamelist.GameListFragment
import com.jonathanl.bgmanager.ui.gamelist.GameListViewModel
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [GameListModule::class],
    dependencies = [MainActivityComponent::class]
)
@FragmentScope
interface GameListComponent {

    fun inject(gameListFragment: GameListFragment)

}

@Module
object GameListModule {

    @Provides
    @FragmentScope
    fun provideGameListViewModel(): GameListViewModel =
        GameListViewModel()

}