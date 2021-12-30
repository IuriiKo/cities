package com.kushyk.test.di

import androidx.annotation.RawRes
import com.google.gson.Gson
import com.kushyk.test.R
import com.kushyk.test.data.CitiesRepository
import com.kushyk.test.data.RawCitiesRepository
import com.kushyk.test.domain.FindCitiesUseCase
import com.kushyk.test.domain.FindCitiesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CitiesModule {
    @Provides
    @Singleton
    @RawRes
    fun provideRawId(): Int = R.raw.cities

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideCitiesRepository(repository: RawCitiesRepository): CitiesRepository = repository


    @Provides
    @Singleton
    fun provideFindCitiesUseCase(useCase: FindCitiesUseCaseImpl): FindCitiesUseCase = useCase
}