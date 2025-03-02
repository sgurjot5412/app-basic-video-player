package com.pubscale.basicvideoplayer.di

import com.pubscale.basicvideoplayer.data.remote.ApiService
import com.pubscale.basicvideoplayer.data.repository.VideoRepositoryImpl
import com.pubscale.basicvideoplayer.domain.repository.VideoRepository.VideoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoRepository(apiService: ApiService): VideoRepo {
        return VideoRepositoryImpl(apiService)
    }
}