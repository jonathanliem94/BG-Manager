package com.jonathanl.bgmanager.di

import android.content.Context
import com.jonathanl.bgmanager.MainActivity
import com.jonathanl.bgmanager.MainActivityViewModel
import com.jonathanl.bgmanager.data.GameListDbService
import com.jonathanl.bgmanager.data.NetworkService
import com.jonathanl.bgmanager.data.Repository
import com.jonathanl.bgmanager.data.RepositoryImpl
import com.jonathanl.bgmanager.useCases.GameListUseCase
import com.jonathanl.bgmanager.useCases.GameListUseCaseImpl
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCaseImpl
import dagger.BindsInstance
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

    @Component.Builder
    interface MainActivityComponentBuilder{
        @BindsInstance
        fun context(context: Context): MainActivityComponentBuilder
        fun build(): MainActivityComponent
    }

}

@Module
object MainActivityModule {

    @Provides
    @ActivityScope
    fun provideRepository(
        networkService: NetworkService,
        gameListDbService: GameListDbService
    ): Repository =
        RepositoryImpl(gameListDbService, networkService)

    @Provides
    @ActivityScope
    fun provideGameListDbService(context: Context): GameListDbService =
        GameListDbService.createDatabaseAccessService(context)

    @Provides
    @ActivityScope
    fun provideNetworkService(): NetworkService =
        NetworkService.createNetworkService()

    @Provides
    @ActivityScope
    fun provideGameListUseCase(repository: Repository): GameListUseCase =
        GameListUseCaseImpl(repository)

    @Provides
    @ActivityScope
    fun provideNetworkUseCase(repository: Repository): NetworkUseCase =
        NetworkUseCaseImpl(repository)

    @Provides
    @ActivityScope
    fun provideMainActivityViewModel(): MainActivityViewModel =
        MainActivityViewModel()

}