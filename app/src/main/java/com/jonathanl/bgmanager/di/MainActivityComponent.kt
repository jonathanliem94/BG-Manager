package com.jonathanl.bgmanager.di

import com.jonathanl.bgmanager.MainActivity
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.repository.Repository
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
    fun provideSharedViewModel(): SharedViewModel

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