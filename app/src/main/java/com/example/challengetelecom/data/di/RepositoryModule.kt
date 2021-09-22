package com.example.challengetelecom.data.di

import android.app.Application
import com.example.challengetelecom.data.network.service.AppService
import com.example.challengetelecom.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        application: Application,
        appService: AppService,
    ): MainRepository {
        return MainRepository(application, appService)
    }

}