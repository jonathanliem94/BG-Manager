package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.MainActivity
import com.jonathanl.bgmanager.MainActivityViewModel
import com.jonathanl.bgmanager.data.NetworkService
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.GameListUseCaseImpl
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCaseImpl
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [MainActivityModule::class]
)
@ActivityScope
interface MainActivityComponent {

    // Dagger has to provide dependencies for these
    fun inject(mainActivity: MainActivity)

    // Make SharedViewModel visible for other components depending on this
    fun provideNetworkUseCase(): NetworkUseCase
    fun provideGameListUseCase(): GameListUseCase

    /* No need for builder for now, as the component has no dependencies
    @Component.Builder
    interface MainAppComponentBuilder{
        fun build(): MainActivityComponent
    }*/

}

@Module
object MainActivityModule {

    @Provides
    @ActivityScope
    fun provideNetworkService(): NetworkService =
        NetworkService.createNetworkService()

    @Provides
    @ActivityScope
    fun provideGameListUseCase(): GameListUseCase =
        GameListUseCaseImpl()

    @Provides
    @ActivityScope
    fun provideNetworkUseCase(networkService: NetworkService): NetworkUseCase =
        NetworkUseCaseImpl(networkService)

    @Provides
    @ActivityScope
    fun provideMainActivityViewModel(): MainActivityViewModel =
        MainActivityViewModel()

}