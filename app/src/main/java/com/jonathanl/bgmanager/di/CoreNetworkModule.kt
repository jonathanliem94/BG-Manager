package com.jonathanl.bgmanager.di

import android.content.Context
import androidx.room.Room
import com.jonathanl.bgmanager.data.DBRepository
import com.jonathanl.bgmanager.data.GameListDbService
import com.jonathanl.bgmanager.useCases.NetworkUseCase
import com.jonathanl.bgmanager.useCases.NetworkUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

const val BASE_URL = "https://www.boardgamegeek.com"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkBuilder(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun provideDatabaseAccessService(@ApplicationContext context: Context): GameListDbService {
        return Room.databaseBuilder(
            context,
            GameListDbService::class.java,
            "BG Manager Database")
            .build()
    }


}