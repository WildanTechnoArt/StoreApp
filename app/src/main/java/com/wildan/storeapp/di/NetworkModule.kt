package com.wildan.storeapp.di

import com.wildan.storeapp.data.api.BaseApiService
import com.wildan.storeapp.data.api.RetrofitClient
import com.wildan.storeapp.repository.AuthRepository
import com.wildan.storeapp.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): BaseApiService {
        return RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun provideProductRepository(client: BaseApiService): ProductRepository {
        return ProductRepository(client)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(client: BaseApiService): AuthRepository {
        return AuthRepository(client)
    }
}
