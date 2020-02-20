package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.ui.search.SearchFragment
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [SearchModule::class],
    dependencies = [MainActivityComponent::class]
)
@FragmentScope
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)

}

@Module
object SearchModule {

    @Provides
    @FragmentScope
    fun provideSearchViewModel(
        networkUseCase: NetworkUseCase,
        gameListUseCase: GameListUseCase
    ): SearchViewModel =
        SearchViewModel(networkUseCase, gameListUseCase)

}