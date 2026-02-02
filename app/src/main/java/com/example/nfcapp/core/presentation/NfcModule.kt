package com.example.nfcapp.core.presentation

import com.example.nfcapp.core.data.remote.ArrivalApiService
import com.example.nfcapp.core.data.repository.ArrivalRepositoryImpl
import com.example.nfcapp.core.data.repository.NfcRepositoryImpl
import com.example.nfcapp.core.data.source.ArrivalRemoteDataSource
import com.example.nfcapp.core.data.source.ReadDataSource
import com.example.nfcapp.core.domain.repository.usecase.ReadClassicCardUseCase
import com.example.nfcapp.core.domain.repository.usecase.SendArrivalUseCase
import com.example.nfcapp.core.domain.repository.ArrivalRepository
import com.example.nfcapp.core.domain.repository.NfcReadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    fun provideDataSource() = ReadDataSource()

    @Provides
    fun provideRepository(
        source: ReadDataSource
    ): NfcReadRepository = NfcRepositoryImpl(source)

    @Provides
    fun provideUseCase(
        repo: NfcReadRepository
    ) = ReadClassicCardUseCase(repo)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://notifynfc-production-382f.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideArrivalApiService(retrofit: Retrofit): ArrivalApiService {
        return retrofit.create(ArrivalApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideArrivalRemoteDataSource(
        apiService: ArrivalApiService
    ) = ArrivalRemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideArrivalRepository(
        remoteDataSource: ArrivalRemoteDataSource
    ): ArrivalRepository = ArrivalRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideSendArrivalUseCase(
        repository: ArrivalRepository
    ) = SendArrivalUseCase(repository)
}