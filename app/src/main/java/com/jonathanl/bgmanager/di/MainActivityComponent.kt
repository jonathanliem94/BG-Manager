package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.MainActivity
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.repository.Repository
import com.jonathanl.bgmanager.ui.gamelist.GameListFragment
import com.jonathanl.bgmanager.ui.search.SearchFragment
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
    fun inject(searchFragment: SearchFragment)
    fun inject(gameListFragment: GameListFragment)

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
    fun provideRepository(): Repository =
        Repository()

    @Provides
    @ActivityScope
    fun provideSharedViewModel(repository: Repository): SharedViewModel =
        SharedViewModel(repository)

}